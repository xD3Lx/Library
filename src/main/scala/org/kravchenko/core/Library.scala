package org.kravchenko.core

import Library._

trait Library {
  type Id = Long

  def addNewBook(book: Book): Id
  def removeBookById(book: Id): Unit
  def list(): List[BookInfo]
  def search(query: SearchQuery): List[BookInfo]
  def lend(id: Id, person: String): LendOperationResult
  def getBookDetailedInfo(id: Id): BookInfoDetailed
}

class LibraryImpl extends Library {

  private var store: Map[Id, Book] = Map.empty

  override def addNewBook(book: Book): Id = ???

  override def removeBookById(book: Id): Unit = ???

  override def list(): List[BookInfo] = ???

  override def search(query: SearchQuery): List[BookInfo] = ???

  override def lend(id: Id, person: String): LendOperationResult = ???

  override def getBookDetailedInfo(id: Id): BookInfoDetailed = ???

  //used only for testing
  private[core] def getStore: List[(Id, Book)] = store.toList
}

object Library {
  case class Book(title: String, year: Int, author: String)

  case class LendInformation(available: Long, lent: Long)
  case class BookInfo(book: Book, info: LendInformation)
  case class BookInfoDetailed(book: Book, lent: Boolean, person: Option[String])

  sealed trait LendOperationResult
  case object LendOperationSucceeded extends LendOperationResult
  case object LendOperationFailed extends LendOperationResult

  case class SearchQuery(title: Option[String], year: Option[Int], author: Option[String])
}