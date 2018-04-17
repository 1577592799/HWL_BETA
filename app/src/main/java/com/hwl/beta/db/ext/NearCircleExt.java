package com.hwl.beta.db.ext;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;

import java.util.List;

/**
 * Created by Administrator on 2018/3/11.
 */

public class NearCircleExt {
    private NearCircle info;
    private List<NearCircleImage> images;
    private List<NearCircleComment> comments;
    private List<NearCircleLike> likes;

    public NearCircleExt() {
    }

    public NearCircleExt(int contentType) {
        this.info = new NearCircle();
        this.info.setContentType(contentType);
    }

    public NearCircleExt(NearCircle info) {
        this.info = info;
    }

    public NearCircleExt(NearCircle info, List<NearCircleImage> images, List<NearCircleComment> comments, List<NearCircleLike> likes) {
        this(info);
        this.images = images;
        this.comments = comments;
        this.likes = likes;
    }

    public NearCircle getInfo() {
        return info;
    }

    public void setInfo(NearCircle info) {
        this.info = info;
    }

    public List<NearCircleImage> getImages() {
        return images;
    }

    public void setImages(List<NearCircleImage> images) {
        this.images = images;
    }

    public List<NearCircleComment> getComments() {
        return comments;
    }

    public void setComments(List<NearCircleComment> comments) {
        this.comments = comments;
    }

    public List<NearCircleLike> getLikes() {
        return likes;
    }

    public void setLikes(List<NearCircleLike> likes) {
        this.likes = likes;
    }
}
