package com.project.Electronic_Store.helper;

import com.project.Electronic_Store.dto.pageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <V, U> pageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){

        List<U> entity = page.getContent();

        List<V> entityRepo = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());

        pageableResponse<V> response = new pageableResponse<>();

        response.setContent(entityRepo);

        response.setPageNumber(page.getNumber()+1);
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getNumberOfElements());
        response.setLastPage(page.isLast());
        return response;

    }
}
