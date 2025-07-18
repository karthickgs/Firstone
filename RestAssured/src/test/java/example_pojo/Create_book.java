package example_pojo;

public class Create_book {
	
	private Book book_details;
	private String copies_sold;
	private String Year_of_Publication;
	private String Publication_Name;
	private Author Author_Details;
	public Book getBook_details() {
		return book_details;
	}
	public void setBook_details(Book book_details) {
		this.book_details = book_details;
	}
	public String getCopies_sold() {
		return copies_sold;
	}
	public void setCopies_sold(String copies_sold) {
		this.copies_sold = copies_sold;
	}
	public String getYear_of_Publication() {
		return Year_of_Publication;
	}
	public void setYear_of_Publication(String year_of_Publication) {
		Year_of_Publication = year_of_Publication;
	}
	public String getPublication_Name() {
		return Publication_Name;
	}
	public void setPublication_Name(String publication_Name) {
		Publication_Name = publication_Name;
	}
	public Author getAuthor_Details() {
		return Author_Details;
	}
	public void setAuthor_Details(Author author_Details) {
		Author_Details = author_Details;
	}

}
