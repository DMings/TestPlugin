### SPlugin（实现的四大组件插件化学习）
------

#### 简单说一下现在的插件化方式历程：

&emsp;1、做一个伪Activity壳，通过代理分发生命周期，灵活性较差,代表有DroidPlugin的that框架。

&emsp;2、通过hook各种framework层实现对坑位的替换，如Instrumentation，H等;hook比较多，兼容性需要适配机型，代表有VirtualAPK。

&emsp;3、只hook一个classloader,hook一点，坑位占用，比较稳定，代表有RePlugin。

&emsp;4、无任何hook,通过代理分发技术，分发生命周期，代表有Phantom。

#### SPlugin学习Relugin思想，代理分发技术，一步一步实现插件化

&emsp;首先插件化在设计分为两种：插件资源与host资源共享，插件资源与host资源独立，下面简单的说一下两个的区别与优缺点。

#### 插件资源与host资源共享

&emsp;这个好处是插件可以访问host资源，节约空间。缺点是资源处理比较麻烦，由于插件资源往往容易与host重复，这就容易造成冲突，冲突分两种：资源文件冲突和class文件冲突，资源冲突就是host的资源ID与插件的一样，这样插件解析资源就会出错。class文件冲突是插件的class与host的class类名一样，插件一旦加载host的类，两个类又不同就容易发生错误。在合并资源的时候，需要反射把插件的Resource加入host中，由于机型存在兼容问题（像小米Resource就做MiuiResource），这个需要进行机型适配。在这个冲突问题处理上，可以使用gradle编写插件去除相同的类，同名资源，往往要对比插件与host资源较为复杂。

#### 插件资源与host资源独立

&emsp;这个好处是插件资源与host资源不会相互影响，缺点是插件不能访问host的资源，冗余比较多。这是由于插件不能访问host，所有东西都由插件自身解决。优点明显，无需对资源进行特别处理，resource是插件独有，不会跟host冲突。同样插件的dex与host中的dex也是独立，只是共用一个parent，pathClassloader是host，dexClassloader是插件，他们相互对立不能访问，关系如下：

parent classloader

        /\
        
 pathClassloader | dexClassloader

------

#### SPlugin的主要设计思想

> * hook classloader 
> * 代理分发
> * 隔离插件与host资源.
 
&emsp;SPlugin参考360的replugin，hook应用中的pathclassloader（application.getBaseContext()）,并替换为自己的，实现了对从app的dex中loader的calss进行拦截。SPlugin的插件的使用了资源隔离的dexclassloader加载，与应用的pathclassloader他们的父类，构成了一个三角形(如上关系)。so库在解释插件apk的时候，根据当前平台，复制对应平台so到主app私有文件夹中，在创建dexClassloader把对应的路径设置进去，避免jni调用失败。
&emsp;插件的四大组件数据从androidmanifest解释获取；从androidmanifest获取组件的类名，内容权限等信息相对容易，但是intent-filter数据无法获取，我猜出于安全吧（隐试数据就是为了不让第三方应用轻易知道），这里由于apk是自己的，可以手动解析，这里用pull解析androidmanifest(SPlugin中自己手写的，用了类似树的结构)，SPlugin解释了广播，为后面广播隐试注册做准备。

#### 项目结构：

&emsp;host     |    plugin

&emsp;hostLib  |    pluginLib

host为主app，依赖一个hostLib库，plugin为插件app，依赖的是pluginLib，hostLib库与hostLib库独立。

#### 以下为Demo，可以下载看效果：

&emsp;插件app可以独立启动（包含jni）：[插件app](https://github.com/DMings/TestPlugin/blob/master/appDemo/src/main/assets/NDK_1.0.8.apk)

&emsp;host在assets已包含插件app:[主app](https://github.com/DMings/TestPlugin/blob/master/appDemo/apk/host_demo.apk)

#### 下面说一下四大组件实现核心过程：

 1. activity的坑位替换，start插件activity，在startactivityforresult中替换为插件的坑位，随后在threadactivity中lunchactivity，new activity，经过类加载器进行替换为真实的activity。这里难点为插件activity的资源问题，如主题，由于在传递主题资源id仍然为主的app的主题id，插件没有主的资源，往往会出错，需要重新指定未插件主题id，其次xml的反射setcontentview也容易出问题，由于factory存在缓存，使用的是主app的classloader,在创建插件的view的时候找不到一些资源，这里使用了重写factory解决，在factory直接使用插件的classloader进行创建。坑位占用Demo设计为在startactivity占用，在classloader换回真实类就释放，占用期不长，如果应用被系统杀死这里恢复机制尚没有做。
 2. service用到技术也是一样。也是用到坑位，不同的是，坑位的占用是伴随service生命周期，当service调用ondestroy才释放坑位，目的保证bindservice,unbindservice正常（由于bindservice,unbindservice需要把类型传入Intent）。
 3. Boastcoast，SPlugin由于不考虑应用在未启动的时候接收广播，在处理上相对容易；广播分静态注册与动态注册，动态注册不需要考虑，需要做的是把静态注册转换为动态注册。在这里，通过解析androidManifest,获取静态注册的广播，解析Intent-filter，然后手动动态注册。
 4. ContentProvider，使用的是代理分发技术。内容提供者存在的意义是跨进程传递数据，由于插件中的内容提供者无法被外部感知，这里需要再提前注册一个ContentProviderProxy，插件中的内容提供者提前注册到一个数组中，外部的应用先通过这个代理通信，解析出目标，从数组中找到目标的内容提供者，然后把需要通信转发到目标ContentProvider，再经过这个ContentProviderProxy传递出去，完成数据传递。
