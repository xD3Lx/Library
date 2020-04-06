package org.kravchenko.core

import org.kravchenko.core.Library.{Book, LendInfo, LendOperationSucceeded}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class LibraryTest extends AnyFlatSpec with Matchers {

  private val book1 = Book("The Fellowship of the Ring", 1954, "J. R. R. Tolkien")
  private val book2 = Book("Night Watch", 1998, "Sergei Lukyanenko")
  private val book3 = Book("The Last Wish", 1993, "Andrzej Sapkowski")

  "A library" should "add one new book" in new TestLibrary {
    //When
    library.addNewBook(book1)

    //Then
    library.getStore must have size 1
    library.getStore must contain ((1, book1))
  }

  it should "add multiple copies of one book" in new TestLibrary {
    //When
    library.addNewBook(book1)
    library.addNewBook(book1)

    //Then
    libraryStore must have size 2
    libraryStore must contain allOf ((1, book1), (2, book1))
  }

  it should "add multiple books" in new TestLibrary {
    //When
    library.addNewBook(book1)
    library.addNewBook(book2)
    library.addNewBook(book3)

    //Then
    libraryStore must have size 3
    libraryStore must contain allOf ((1, book1), (2, book2), (3, book3))
  }

  it should "delete existing book from the library" in new TestLibrary {
    //Given
    library.addNewBook(book1)

    //When
    library.removeBookById(1L)

    //Then
    libraryStore must have size 0
  }

  it should "not allow to delete a book which is lent" in new TestLibrary {
    //Given
    val id = library.addNewBook(book1)
    library.lend(id, "some person")

    //When
    library.removeBookById(id)

    //Then
    libraryStore must have size 1
  }

//  Should allow to lent a book by ID ( should be forbidden if copy with given ID is already lent). Should allow to pass the name of the person who lend the book.

  it should "allow to lend a book which is not yet lent" in new TestLibrary {
    //Given
    val id = library.addNewBook(book1)
    val person = "Michael"

    //When
    val result = library.lend(id, person)

    //Then
    result must be(Library.LendOperationSucceeded)
    lendStore must have size 1
    lendStore must contain(id, LendInfo(person))
  }

  it should "not allow to lend a book which is already lent" in new TestLibrary {
    //Given
    val id = library.addNewBook(book1)
    val person = "Michael"
    library.lend(id, person)

    //When
    val result = library.lend(id, person)

    //Then
    result must be(Library.LendOperationFailed)
    lendStore must have size 1
  }



}

trait TestLibrary {
  val library: LibraryImpl = new LibraryImpl
  def libraryStore: Seq[(Long, Book)] = library.getStore
  def lendStore: Seq[(Long, LendInfo)] = library.getLendStore
}
