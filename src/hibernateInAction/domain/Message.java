package hibernateInAction.domain;

import javax.persistence.*;

/**
 * Name: admin
 * Date: 2017/3/22
 * Time: 13:21
 */
@Entity(name = "Message")
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "NEXT_MESSAGE_ID")//指定加入一个实体的关联或嵌入式集合时使用的外键列
    private Message nextMessage;

    public Message(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message getNextMessage() {
        return nextMessage;
    }

    public void setNextMessage(Message nextMessage) {
        this.nextMessage = nextMessage;
    }
}
