package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
        // タスク名と期限があるToDoを取得する(不要なのでは？)
        // List<ToDo> findByTaskNameContainingOrLimitDateContaining(String taskName,
        // String limitDate);

        /*
         * List<ToDo>:ToDo型のデータを複数まとめて管理する
         * findByCompletedAtIsNull():completedAtがnullのデータをデータベースで検索して取得するメソッド
         */
        // 完了日がnullのToDoを取得する
        List<ToDo> findByCompletedAtIsNull();
}
