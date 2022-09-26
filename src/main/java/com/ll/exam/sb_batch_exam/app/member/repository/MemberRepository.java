package com.ll.exam.sb_batch_exam.app.member.repository;


import com.ll.exam.sb_batch_exam.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
}
