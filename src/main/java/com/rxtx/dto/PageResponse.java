package com.rxtx.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/4/22 9:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class PageResponse<T> extends BaseResponse {


    //private PagedData<T> data;

    /*public static <T> PageResponse<T> of(Page<T> page, long current, long pageSize) {
        PagedData<T> data = new PagedData<>(page == null ?
                new Page<T>().setCurrent(current).setSize(pageSize): page);
        return new PageResponse<>(data);
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return of(page,page.getCurrent(),page.getSize());
    }

    public static <T> PageResponse<T> of(PageInfo<T> page) {
        PagedData<T> data = new PagedData<>(page);
        return new PageResponse<>(data);
    }


    private PageResponse(PagedData<T> data) {
        super(AppStatus.SUCCESS);
        this.data = data;
    }


    @Data
    public static class PagedData<T> {
        @ApiModelProperty("分页后的内容")
        private List<T> content;

        @ApiModelProperty(value = "当前页面序号", example = "1")
        private Long page;

        @ApiModelProperty(value = "当前页面大小", example = "20")
        private Long size;

        @ApiModelProperty(value = "总页面数", example = "5")
        private Long pages;

        @ApiModelProperty(value = "总记录数", example = "100")
        private Long total;

        PagedData(Page<T> page) {
            this.content = page.getRecords();
            this.page = page.getCurrent();
            this.size = page.getSize();
            this.pages = page.getPages();
            this.total = page.getTotal();
        }

        PagedData(PageInfo<T> page) {
            this.content = page.getList();
            this.page = (long) page.getPageNum();
            this.size = (long) page.getSize();
            this.pages = (long) page.getPages();
            this.total = page.getTotal();
        }

    }*/
}
