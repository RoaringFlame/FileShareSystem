package com.fss.dao.repositories;

import com.fss.dao.domain.FileReceive;

import java.util.List;

public interface FileReceiveRepository extends BaseRepository<FileReceive> {

    FileReceive findByReceiverIdAndFileId(String receiverId,String fileId);

    List<FileReceive> findByFileId(String fileId);
}
