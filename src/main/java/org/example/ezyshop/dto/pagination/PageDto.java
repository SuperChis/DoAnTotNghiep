package org.example.ezyshop.dto.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageDto {
    private boolean last;
    private long totalElements;
    private int	totalPages;
    private int	size;
    private int	number;
    //    private Sort sort;
    private int	numberOfElements;
    private boolean	first;

    public static PageDto populatePageDto(Page<?> page) {
        if (page == null) {
            return null;
        }

        PageDto pageDto = new PageDto();

        pageDto.setLast(page.isLast());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setSize(page.getSize());
        pageDto.setNumber(page.getNumber() + 1);
//        pageDto.setSort(page.getSort());
        pageDto.setNumberOfElements(page.getNumberOfElements());
        pageDto.setFirst(page.isFirst());
        pageDto.setTotalPages(page.getTotalPages());

        return pageDto;
    }
}

