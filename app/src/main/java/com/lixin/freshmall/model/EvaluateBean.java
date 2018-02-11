package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/6
 * My mailbox is 1403241630@qq.com
 */

public class EvaluateBean {
    /**
     *
     请求格式
     {
     cmd:"getCommodityComment"
     commodityid:"123"//商品id
     nowPage:"1" //当前页数
     }
     返回格式
     {
     result:"0" //0成功1失败
     resultNote:"失败原因"
     totalPage:5//总页数
     commentList:"[{
     commentIcon:""//评论人头像
     commentNickName:""//评论人昵称
     commentTime:""//评论时间
     commentContent:""//评论内容
     commentScore:""//评论分数
     commentReply:""//评论商家回复
     commentPicture:"[{//评论图片集合
     img:"http://jdsadksja.png"
     }]"
     }]"
     }
     */

    private String result;
    private String resultNote;
    private int totalPage;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<CommentList> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentList> commentList) {
        this.commentList = commentList;
    }

    private List<CommentList> commentList;
    public static class CommentList{
        private String commentIcon;
        private String commentNickName;
        private String commentContent;
        private String commentTime;

        public String getCommentIcon() {
            return commentIcon;
        }

        public void setCommentIcon(String commentIcon) {
            this.commentIcon = commentIcon;
        }

        public String getCommentNickName() {
            return commentNickName;
        }

        public void setCommentNickName(String commentNickName) {
            this.commentNickName = commentNickName;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getCommentTime() {
            return commentTime;
        }

        public void setCommentTime(String commentTime) {
            this.commentTime = commentTime;
        }
    }
}
