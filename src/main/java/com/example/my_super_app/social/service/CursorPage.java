package com.example.my_super_app.social.service;

import java.util.List;

public record CursorPage<T>(
        List<T> items,
        Long nextCursor,
        boolean hasMore
) {}
