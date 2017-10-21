package com.fss.dao.repositories;

import com.fss.dao.domain.FileReceive;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileReceiveRepository extends BaseRepository<FileReceive> {

    @Query(value="select * from t_file_receive fr where fr.user_id=?1 and fr.version_id =?2",nativeQuery=true)
    FileReceive getReceiveBy(String userId, String versionId);

    List<FileReceive> findByFileId(String fileId);

    void deleteByReceiverIdAndFileId(String receiverId,String fileId);
}
