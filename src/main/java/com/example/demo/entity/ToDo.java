package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "todo")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName; /* タスク名 */

    private String limitDate; /* 期限 */

    private String detail; /* 詳細 */

    private LocalDateTime createdAt; /* 作成日 */

    private LocalDateTime updateAt; /* 更新日 */

    private LocalDateTime completedAt; /* 完了日 */
}