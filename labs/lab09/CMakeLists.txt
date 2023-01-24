cmake_minimum_required(VERSION 3.0)
project(lab09)


set(CMAKE_CXX_STANDARD 20)


add_executable(lab09 lex.yy.c)
target_link_libraries(lab09 -ll)

#[[
cmake file copy directory content https://stackoverflow.com/questions/13429656/how-to-copy-contents-of-a-directory-into-build-directory-after-make-with-cmake
needs to be placed after add_executable()
]]
add_custom_command(TARGET lab09 PRE_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy_directory
        ${CMAKE_SOURCE_DIR}/input-code/ $<TARGET_FILE_DIR:lab09>)
