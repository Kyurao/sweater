package com.kyurao.sweater.repository;

import com.kyurao.sweater.domain.DialogMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogMsgRepository extends JpaRepository<DialogMsg, Long> {

}
