package com.woojun.cartoon_four_cut

enum class FilterTypes(val filterName: String) {
    None("필터 X"),
    Test1("필터 O - Test1"),
    Test2("필터 O - Test2"),
    Test3("필터 O - Test3"),
    Test4("필터 O - Test4"),
    Disney("Disney A.I 필터"),
    Anime("Anime A.I 필터"),
    Comics("Comics A.I 필터"),
    Flat2d("Flat2d A.I 필터"),
    Real3d("Real3d A.I 필터");
    companion object {
        fun fromFilterName(filterType: FilterTypes): String {
            return filterType.filterName
        }
        fun fromNameFilter(name: String): FilterTypes {
            return entries.find { it.filterName == name }!!
        }
    }
}