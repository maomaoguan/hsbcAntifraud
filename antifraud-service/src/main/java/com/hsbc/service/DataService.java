package com.hsbc.service;

import java.util.List;
import java.util.Map;

/**
 * as in an examination, using this as a representation as data,
 * which shall be replaced with remote data in a real system
 */
public interface DataService {
    /**
     * data engine initiations.
     * data will be loaded in a pre-loading manner
     */
    public void init();

    List<Map<String, Object>> findDataByAccount(String userId);
}
