
cmake_minimum_required(VERSION 3.4.1)


add_library( testjni
             SHARED
            src/main/cpp/TestJni.cpp)


find_library( log-lib
              log )


target_link_libraries( testjni
                       ${log-lib} )