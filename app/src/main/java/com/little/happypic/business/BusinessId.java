package com.little.happypic.business;

/**
 * Created by little on 16/3/12.
 */
public class BusinessId {

    // 每个业务分配BusiId区间大小
    private static final int ID_SECTION_SIZE = 200;


    //---------------------------------Pic related  ---------------------------------
    public final static class Pic {
        private static final int ID_BASE = 0 * ID_SECTION_SIZE;

        public static final int ID_PIC_CLASSES = ID_BASE + 1;
        public static final int ID_PIC_CLASS_LIST = ID_BASE + 2;
        public static final int ID_PIC_LIST = ID_BASE + 3;
    }
    //---------------------------------Pic related end -------------------------------
}
