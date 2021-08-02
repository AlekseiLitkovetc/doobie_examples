package ru.alitkovetc.doobie.example

trait WorldTransactorWithYOLO extends WorldTransactor {

  protected val yolo = xa.yolo

}
