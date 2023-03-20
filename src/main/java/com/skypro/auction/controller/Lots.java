package com.skypro.auction.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lot")
public class Lots {

//    @GetMapping("/{id}/first") //Получить информацию о первом ставившем на лот
//    @GetMapping("/{id}/frequent") //Возвращает имя ставившего на данный лот наибольшее количество раз
//    @GetMapping("/{id}") //Получить полную информацию о лоте

//    @PostMapping("/{id}/start") //Начать торги по лоту
//    @PostMapping("/{id}/bid") //Сделать ставку по лоту
//    @PostMapping("/{id}/stop") //Остановить торги по лоту
//    @PostMapping //Создает новый лот

//    @GetMapping  //Получить все лоты, основываясь на фильтре статуса и номере страницы
//    @GetMapping("/export") //Экспортировать все лоты в файл CSV



}
