package org.kravchenko.core

import org.kravchenko.core.Library.Book
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class LibraryTest extends AnyFlatSpec with Matchers {

  private val book1 = Book("The Fellowship of the Ring", 1954, "J. R. R. Tolkien")
  private val book2 = Book("Night Watch", 1998, "Sergei Lukyanenko")
  private val book3 = Book("The Last Wish", 1993, "Andrzej Sapkowski")

  "A library" should "correctly add one new book" in new TestLibrary {
    //When
    library.addNewBook(book1)

    //Then
    library.getStore must have size 1
    library.getStore must contain ((1, book1))
  }

  it should "correctly add multiple copies of one book" in new TestLibrary {
    //When
    library.addNewBook(book1)
    library.addNewBook(book1)

    //Then
    libraryStore must have size 2
    libraryStore must contain allOf ((1, book1), (2, book1))
  }

  it should "correctly add multiple books" in new TestLibrary {
    //When
    library.addNewBook(book1)
    library.addNewBook(book2)
    library.addNewBook(book3)

    //Then
    libraryStore must have size 3
    libraryStore must contain allOf ((1, book1), (2, book2), (3, book3))
  }

}

trait TestLibrary {
  val library: LibraryImpl = new LibraryImpl
  def libraryStore: Seq[(Long, Book)] = library.getStore
}
