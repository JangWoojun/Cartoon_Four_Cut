package com.woojun.cartoon_four_cut

enum class FrameTypes (frameName: String) {
    None("프레임 X"),
    Test1("프레임 O - Test1"),
    Test2("프레임 O - Test2"),
    Test3("프레임 O - Test3"),
    Test4("프레임 O - Test4");
    companion object {
        fun fromFrameName(frameName: FrameTypes): String {
            return FrameTypes.entries.find {it.name == frameName.name}?.name!!
        }
    }
}