package com.khasang.vkphoto.presentation.view;

import com.khasang.vkphoto.presentation.model.Comment;

import java.util.List;

/**
 * Created by admin on 07.03.2016.
 */
public interface VkCommentsView extends VkView{

    void displayVkComments(List<Comment> comments);
}
