package rmuti.askexpert.model.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "like_data")
public class LikeData {
    @Id
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "uuid2")
    @Column(name = "like_id",length = 36,nullable = false,updatable = false)
    private String likeId;

    @Column(name = "like_contentId",updatable = false,nullable = false)
    private String contentId;

    @Column(name = "like_ownerId",updatable = false,nullable = false)
    private String ownerId;

    @Column(name = "like_status",nullable = false)
    private byte status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate = new Date();

    //TODO: ลบเลยถ้าเกิดไม่มีการ like หรือจะ ให้มันมีการนับการ like ตลอดเลยดีนะ

}