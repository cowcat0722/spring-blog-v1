package shop.mtcoding.blog._core;

public class PagingUtil {

    // 마지막 페이지 인지
    public static boolean isLast(int currentPage, int totalCount){
        int totalPageCount = getTotalPageCount(totalCount);
        return currentPage == totalPageCount ? true : false;
    }

    // 첫 번째 페이지 인지
    public static boolean isFirst(int currentPage){
        return currentPage == 0 ? true : false;
    }

    // 전체 페이지 개수 리턴
    public static int getTotalPageCount(int totalCount){
        int totalPageCount = (totalCount-1)/ Constant.PAGING_COUNT;
        return totalPageCount;
    }


}
