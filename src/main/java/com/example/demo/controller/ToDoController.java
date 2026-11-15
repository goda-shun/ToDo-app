package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.ToDo;
import com.example.demo.repository.ToDoRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor

public class ToDoController {

  private final ToDoRepository toDoRepository;

  /* ToDo一覧画面表示処理 */
  /*
   * @:アノテーション
   * GetMapping:GETリクエスト
   * ("/"):対象となるURL
   */
  @GetMapping("/")
  /*
   * public:他のクラスからアクセスできる(アクセス修飾子)
   * String:文字列型の値を返す(返すデータの型)
   * list:メソッド名(自由に決めれる)
   * Model:SpringBootのSpringMVCが提供するクラス
   * model:Modelクラスのインスタンスを受け取るための変数名(ControllerからHTMLに渡すための入れ物)
   */

  // アクセス修飾子 データ型 メソッド名 (クラス名 入れ物)
  public String list(Model model) {
    /*
     * List:複数のデータをまとめて管理する
     * <ToDo>:ToDo型のデータを格納する
     * todos:変数名
     * toDoRepository:データベースへのアクセスを担当するRepository
     * findBy:条件に一致するデータを検索する
     * CompletedAtIsNull():CompletedAt(完了日時)がNull(空欄)であること
     */
    List<ToDo> todos = toDoRepository.findByCompletedAtIsNull();
    // データの型 変数名 = データベースへのアクセスおよびデータ取得 ← ToDoRepositoryのfindByCompletedAtメソッドを実行する

    /*
     * model:画面に渡すデータを管理するオブジェクト
     * addAttribute:Modelにデータを追加するメソッド
     * "todos":HTML側で使用する名前
     * todos:Java側で取得したToDoデータ
     */
    model.addAttribute("todos", todos);
    /*
     * return:値を返す
     * "User/index":表示するテンプレート名
     */
    return "User/index";
  }

  /* ToDo新規登録画面表示処理 */
  @GetMapping("/new")
  public String newForm(Model model) {
    model.addAttribute("todo", new ToDo());
    return "User/ToDo-form";
  }

  /* ToDo登録処理 */
  @PostMapping("/create")
  public String create(@ModelAttribute @Valid ToDo todo, BindingResult result) {
    if (result.hasErrors()) {
      return "User/ToDo-form";
    }

    // todo.setTaskName(todo.getTaskName());
    // todo.setLimitDate(todo.getLimitDate());
    // todo.setDetail(todo.getDetail());

    todo.setCreatedAt(LocalDateTime.now());
    todo.setUpdateAt(LocalDateTime.now());
    toDoRepository.save(todo);
    return "redirect:/";
  }

  /* ToDo削除処理 */
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id, HttpServletResponse response) {
    // 指定したidのToDoが存在する場合
    if (toDoRepository.existsById(id)) {
      // 指定したidのToDoをDBから削除する
      toDoRepository.deleteById(id);
    } else {
      // 存在しない場合、エラーを返す
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return "not-found";
    }
    return "redirect:/";
  }

  /* ToDo詳細画面表示処理 */
  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model, HttpServletResponse response) {
    if (model.containsAttribute("todo")) {
      return "User/ToDo-form";
    }

    return toDoRepository.findById(id).map(todo -> {
      model.addAttribute("todo", todo);
      return "User/ToDo-form";
    })
        .orElseGet(() -> {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          return "not-found";
        });
  }

  /* ToDo更新処理 */
  @PostMapping("/update/{id}")
  public String update(@PathVariable Long id, @ModelAttribute @Valid ToDo todo, BindingResult result,
      HttpServletResponse response, RedirectAttributes redirectAttributes) {
    Optional<ToDo> opt = toDoRepository.findById(id);
    if (opt.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return "not-found";
    }

    ToDo todoToUpdate = opt.get();

    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.todo", result);
      redirectAttributes.addFlashAttribute("todo", todo);
      return "redirect:/edit/" + id;
    }

    todoToUpdate.setTaskName(todo.getTaskName());
    todoToUpdate.setLimitDate(todo.getLimitDate());
    todoToUpdate.setDetail(todo.getDetail());
    todoToUpdate.setUpdateAt(LocalDateTime.now());
    toDoRepository.save(todoToUpdate);

    return "redirect:/";
  }

  /* ToDo完了処理(一覧画面から論理削除する) */
  // DBにはデータが残っている状態にする
  @PostMapping("/complete/{id}")
  public String complete(@PathVariable Long id, HttpServletResponse response) {
    Optional<ToDo> opt = toDoRepository.findById(id);
    if (opt.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return "not-found";
    }

    ToDo todo = opt.get();
    todo.setCompletedAt(LocalDateTime.now());

    toDoRepository.save(todo);

    return "redirect:/";
  }

}
