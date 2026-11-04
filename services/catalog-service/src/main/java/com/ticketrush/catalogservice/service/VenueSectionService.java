package com.ticketrush.catalogservice.service;

import com.ticketrush.catalogservice.dto.request.VenueSectionCreateRequest;
import com.ticketrush.catalogservice.dto.request.VenueSectionUpdateRequest;
import com.ticketrush.catalogservice.dto.response.VenueSectionResponse;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VenueSectionService {

    VenueSectionResponse createSection(UUID venueId, VenueSectionCreateRequest request);
    VenueSectionResponse updateSection(UUID venueId, UUID sectionId, VenueSectionUpdateRequest request);
    VenueSectionResponse getSection(UUID venueId, UUID sectionId);
    PageResponse<VenueSectionResponse> listSections(UUID venueId, Pageable pageable);
}