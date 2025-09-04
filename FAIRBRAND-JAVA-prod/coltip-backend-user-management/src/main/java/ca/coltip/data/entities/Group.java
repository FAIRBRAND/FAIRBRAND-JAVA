package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name="groups")
public class Group {

    @Id
    @Column(name="id_group")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name="group_name", length = 100)
    private String groupName;

    @NonNull
    @Column(name="record_status")
    private int recordStatus=1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(@NonNull String groupName) {
        this.groupName = groupName;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }
}
