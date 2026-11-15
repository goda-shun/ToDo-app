package com.example.demo.form;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ToDo {
    private Long id;

    private String taskName; /* タスク名 */

    private String limitDate; /* 期限 */

    private String detail; /* 詳細 */

    private LocalDateTime createdAt; /* 作成日 */

    private LocalDateTime updateAt; /* 更新日 */
}
