package cn.weedien.component.common.convention.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRespDTO<T> {

    /**
     * 当前页
     */
    private Long current;

    /**
     * 每页显示条数
     */
    private Long size = 10L;

    /**
     * 总数
     */
    private Long total;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    public PageRespDTO(long current, long size) {
        this(current, size, 0);
    }

    public PageRespDTO(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    /**
     * 将PageRespDTO<T>转换为PageRespDTO<R>
     * <p>用途：将持久层对象转换为DTO对象
     *
     * @param function 转换函数
     * @param <R>      转换后的类型
     * @return PageRespDTO<R>
     */
    public <R> PageRespDTO<R> convert(Function<? super T, ? extends R> function) {
        List<R> collect = records.stream().map(function).collect(Collectors.toList());
        return PageRespDTO.<R>builder()
                .current(current)
                .size(size)
                .total(total)
                .records(collect)
                .build();
    }
}
