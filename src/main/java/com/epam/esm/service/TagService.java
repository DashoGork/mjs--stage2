package com.epam.esm.service;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class TagService implements Service<Tag>{
    private TagDao tagDao;

    @Autowired
    public TagService(TagDaoImplementation tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public void create(Tag tag){
        tagDao.update(tag.getName());
    }

    public void delete(Tag tag){
        tagDao.delete(tag.getId());
    }

    public List<Tag> read(){
        return tagDao.read();
    }

//    public Tag read(Tag tag){
//        if(tag.getName()!=null){
//            try {
//                return tagDao.read(tag.getName());
//            }catch (TagNotFoundException e){
//
//            }
//        }else
//    }



}
