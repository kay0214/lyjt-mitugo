package co.yixiang.paginator;

import lombok.NoArgsConstructor;

/**
 * 分页器，根据page,limit,totalCount用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 *
 * @author badqiu
 * @author miemiedev
 */
@NoArgsConstructor
public class Paginator implements java.io.Serializable {
    
    private static final long serialVersionUID = 6089482156906595931L;

    /** 分页大小 */
    private static final int DEFAULT_SIZE = 10;

    private int limit = DEFAULT_SIZE;

    private int pageSize;

    /** 总页数 */
    private int totalPages;

    /** 总记录数 */
    private int totalCount;

    /** 是否是第一页 */
    private boolean firstPage;

    /** 是否有后一页 */
    private boolean hasNextPage;

    /** 是否有前一页 */
    private boolean hasPrePage;

    /** 是否是最后页 */
    private boolean lastPage;

    /** 是否是禁用的页 */
    private boolean disabledPage;

    /** 页数 */
    private int currPage;

    /** 偏移行 */
    private int offset;

    /**
     * @param currPage
     *            当前第几页
     * @param pageSize
     *            总件数
     */
    public Paginator(int currPage, int pageSize) {
        super();
        this.pageSize = computeLastPageNumber(pageSize);
        this.currPage = computePageNo(currPage);
    }

    /**
     * @param currPage
     *            当前第几页
     * @param pageSize
     *            一页显示多少
     * @param totalCount
     *            总件数
     */
    public Paginator(int currPage, int totalCount, int pageSize) {
        super();
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.currPage = computePageNo(currPage);
    }


    protected int computePageNo(int currPage) {
        if (currPage <= 1) {
            return 1;
        }
        return currPage;
    }

    private int computeLastPageNumber( int pageSize) {
        if (pageSize == 0) {
            return limit;
        }
        return pageSize;
    }

    /**
     * 取得当前页。
     */
    public int getCurrPage() {
        return currPage;
    }

    /**
     * 取得最小。
     */
    public int getPageSize() {
        if(pageSize==-1) {
            return totalCount;
        }else {
            return pageSize;
        }
    }

    /**
     * 取得总项数。
     *
     * @return 总项数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * offset，计数从0开始，可以用于mysql分页使用(0-based)
     */
    public int getOffset() {
        offset = currPage > 0 ? (currPage - 1) * getLimit() : 0;
        return offset;
    }

    /**
     * 取得最小。
     */
    public int getLimit() {
        if(pageSize==-1) {
            return totalCount;
        }else {
            return pageSize;
        }
    }


}
