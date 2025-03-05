package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.PostWanted;
import com.kekecha.xiantu.mapper.PostWantedMapper;
import com.kekecha.xiantu.service.IPostWantedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostWantedServiceImpl implements IPostWantedService {
    @Autowired
    PostWantedMapper postWantedMapper;

    public List<PostWanted> select(String filter, String city)
    {
        return postWantedMapper.select(filter,city);
    }

    public int insert(PostWanted postWanted)
    {
        return postWantedMapper.insert(postWanted);
    }

    public int update(PostWanted postWanted)
    {
        return postWantedMapper.update(postWanted);
    }

    public int delete(int id)
    {
        return postWantedMapper.delete(id);
    }
}
