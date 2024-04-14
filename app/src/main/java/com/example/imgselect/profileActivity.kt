package com.example.imgselect

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction


class ProfileData(context: Context) {
    private val preferences: SharedPreferences =context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )
    private val editor: SharedPreferences.Editor =preferences.edit()

    private val keyname="name"


    var name
        get() = preferences.getString(keyname,"").toString()
        set(value) {
            editor.putString(keyname,value)
            editor.commit()
        }
    var interests = mutableListOf<String>()

}

@Dao
interface WebsiteRecommand{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertgenre(genre: Genre)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertwebsite(website: Website)
    @Transaction
    @Query("SELECT * FROM genre WHERE genreName=:genreName")
    suspend fun getGenrewithwebsite(genreName: String):List<Genrewithwebsite>
}
@Entity
data class Genrewithwebsite(
    @Embedded val genre: Genre,
    @Relation(
        parentColumn = "genreName",
        entityColumn = "genreName"
    )
    val website:List<Website>
)
@Entity
data class Website(

    @PrimaryKey(autoGenerate = false)
    val Title : String="",
    val Link : String="",
    val Description : String="",
    val genreName: String=""
)

@Entity
data class Genre(
    @PrimaryKey(autoGenerate = false)
    val genreName :String="",
)

val websites = listOf(
    Website(
        genreName="Fiction",
        Title = "Wattpad",
        Link = "https://www.wattpad.com/",
        Description = "Wattpad: Your gateway to a world of limitless stories, where imagination knows no bounds"
    ),
    Website(
        genreName="Fiction",
        Title = "Archive of Our Own (AO3)",
        Link = "https://archiveofourown.org/",
        Description = "AO3: A nonprofit fanfiction archive, hosting a diverse array of user-uploaded fanworks across various fandoms"
    ),
    Website(
        genreName="Fiction",
        Title = "Medium",
        Link = "https://medium.com/",
        Description = "Explore a universe of imagination and emotion with our diverse collection of fiction spanning genres and captivating storytelling."
    ),
    Website(
        genreName="Fiction",
        Title = "Literary Hub",
        Link = "https://lithub.com/",
        Description = "Literary Hub: Your go-to destination for literary news, essays, interviews, and book recommendations from the world's most renowned authors"
    ),
    Website(
        genreName="Fiction",
        Title = "The New Yorker",
        Link = "http://www.newyorker.com/",
        Description = "Where imagination thrives, characters breathe, and stories unfold in a captivating world of literary wonder"
    ),
    Website(
        genreName="Fiction",
        Title = "Electric Literature",
        Link = "https://electricliterature.com/",
        Description = "Electric Literature: A vibrant digital platform showcasing innovative storytelling, diverse voices, and literary excellence across various genres and forms."
    ),
    Website(
        genreName="Fiction",
        Title = "Tor.com",
        Link = "https://www.tor.com/",
        Description = "Tor: Your premier destination for science fiction and fantasy literature, offering articles, short fiction, and book reviews."
    ),
    Website(
        genreName="Fiction",
        Title = "Project Gutenberg",
        Link = "https://www.gutenberg.org/",
        Description = "Project Gutenberg: The world's largest digital library offering over 60,000 free eBooks, including timeless classics and lesser-known gems, accessible to all."
    ),
    Website(
        genreName="Fiction",
        Title = "Short Fiction",
        Link = "https://www.shortfictionbreak.com/",
        Description = "Short Fiction Break: A vibrant online community for writers and readers, featuring original short stories and resources to inspire creativity."
    ),
    Website(
        genreName="Fiction",
        Title = "The Paris Review",
        Link = "http://www.theparisreview.org/",
        Description = "The Paris Review: A prestigious literary magazine renowned for its insightful interviews, poetry, fiction, and essays by celebrated and emerging writers."
    ),
    Website(
        genreName="Novel",
        Title = "Project Gutenberg",
        Link = "https://www.gutenberg.org/",
        Description = "Offers over 60,000 free eBooks, including many classic novels in the public domain.\n"
    ),
    Website(
        genreName="Novel",
        Title = "ManyBooks",
        Link = "https://manybooks.net/",
        Description = " Provides thousands of free eBooks, including novels, in various genres."
    ),
    Website(
        genreName="Novel",
        Title = "BookBub",
        Link = "https://www.bookbub.com/",
        Description = " Offers deals on eBooks, including free novels, tailored to your reading preferences."
    ),
    Website(
        genreName="Novel",
        Title = "Free-eBooks.ne",
        Link = "https://www.free-ebooks.net/",
        Description = "Provides a vast collection of free eBooks, including novels, in various categories."
    ),
    Website(
        genreName="Novel",
        Title = "Open Library",
        Link = "https://openlibrary.org/",
        Description = "A project of the Internet Archive, offering over 1.7 million free eBooks, including novels, that are out of copyright or available for borrowing."
    ),
    Website(
        genreName="Novel",
        Title = "ManyBooks.net",
        Link = "https://manybooks.net/",
        Description = "Offers a wide selection of free eBooks, including novels, in various genres."
    ),

    Website(
        genreName="Novel",
        Title = "Feedbooks",
        Link = "https://www.feedbooks.com/",
        Description = "Provides public domain novels as well as contemporary titles for purchase or free download."
    ),
    Website(
        genreName="Novel",
        Title = "Smashwords:",
        Link = "https://www.smashwords.com/",
        Description = " A platform for independent authors to publish and distribute their eBooks, offering a wide selection of novels across different genres.\n"
    ),

    Website(
        genreName="Novel",
        Title = "FictionPress",
        Link = "https://www.fictionpress.com/",
        Description = "A platform for amateur writers to share their original fiction works, including novels, with readers for free."
    ),

    Website(
        genreName="Novel",
        Title = "Royal Road",
        Link = "https://www.royalroad.com/",
        Description = "A platform for web novels and fan fiction, offering a wide range of genres and styles for free reading."
    ),

    Website(
        genreName="Narrative",
        Title = "Narratively",
        Link = "https://narratively.com/",
        Description = "A platform that prides itself on human-interest stories, offering a mix of personal narratives, investigative journalism, and cultural explorations.\n"
    ),

    Website(
        genreName="Narrative",
        Title = "Longreads",
        Link = "https://longreads.com/",
        Description = "Curates and publishes long-form narrative journalism, essays, and fiction from various publications and authors."
    ),

    Website(
        genreName="Narrative",
        Title = "Narrative Magazine",
        Link = "https://www.narrativemagazine.com/",
        Description = " Publishes fiction, poetry, and nonfiction by established and emerging writers, as well as interviews and writing contests."
    ),

    Website(
        genreName="Narrative",
        Title = "The Moth",
        Link = "https://themoth.org/",
        Description = "Features true stories told live on stage, as well as a podcast and written stories on their website."
    ),

    Website(
        genreName="Narrative",
        Title = "StoryCorps",
        Link = "https://storycorps.org/",
        Description = "Showcases real-life stories collected through interviews conducted by everyday people, often touching on personal experiences, family histories, and cultural insights."
    ),

    Website(
        genreName="Narrative",
        Title = "Narratively Creative",
        Link = "https://narrativelycreative.com/",
        Description = " Offers branded storytelling services, creating compelling narratives for businesses and organizations."
    ),
    Website(
        genreName="Narrative",
        Title = "Narrative.ly:",
        Link = "http://narrative.ly/",
        Description = "Focuses on in-depth narrative journalism and storytelling, covering a wide range of topics and issues."
    ),
    Website(
        genreName="Narrative",
        Title = "The Story Collider",
        Link = "https://www.storycollider.org/",
        Description = " Features true, personal stories about science, told live on stage and through podcasts."
    ),
    Website(
        genreName="Narrative",
        Title = "Lapham's Quarterly",
        Link = "https://www.laphamsquarterly.org/",
        Description = "Publishes historical narratives, essays, and excerpts from primary sources, organized around themes from different time periods."
    ),
    Website(
        genreName="Narrative",
        Title = "This American Life",
        Link = "https://www.thisamericanlife.org/",
        Description = "A weekly public radio show and podcast that features narrative storytelling, often centered around a theme for each episode."
    ),
    Website(
        genreName="History",
        Title = "Historical Novel Society",
        Link = "https://historicalnovelsociety.org/",
        Description = "Provides reviews, features, and news about historical fiction novels and authors."
    ),
    Website(
        genreName="History",
        Title = "Goodreads",
        Link = "https://www.goodreads.com/",
        Description = "Offers a section dedicated to historical fiction where you can find lists of popular historical novels, reader reviews, and recommendations."
    ),

    Website(
        genreName="Historical Fiction",
        Title = "BookBub",
        Link = "https://www.bookbub.com/",
        Description = "Offers deals on historical fiction eBooks, including discounted and free titles, tailored to your reading preferences."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "Historical Fiction Online",
        Link = "https://historicalfictiononline.com/",
        Description = "A forum where historical fiction enthusiasts can discuss their favorite books, share recommendations, and connect with fellow readers."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "Victorian Web",
        Link = "http://www.victorianweb.org/",
        Description = "A resource for information about the Victorian era, including literature, history, and culture, which can be valuable for readers and writers of historical fiction set in this period."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "Medievalists.net",
        Link = "https://www.medievalists.net/",
        Description = "Offers articles, book reviews, and resources related to medieval history and culture, which can be useful for fans of historical fiction set in the Middle Ages."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "Renaissance Historical Fiction",
        Link = "https://www.renaissancehistoricalfiction.com/",
        Description = "A website dedicated to historical fiction set in the Renaissance period, offering book recommendations, author interviews, and articles about the era."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "Historical Fiction eBooks",
        Link = "https://historical-fiction.com/",
        Description = "Provides a curated selection of historical fiction eBooks for purchase and download, along with author interviews and features."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "The Historical Fiction Company",
        Link = "https://www.hfco.store/",
        Description = "A platform that connects readers with historical fiction books and authors, offering a range of titles across different historical periods."
    ),
    Website(
        genreName="Historical Fiction",
        Title = "Reading the Past",
        Link = "https://readingthepast.com/",
        Description = "A blog dedicated to historical fiction, featuring reviews, author interviews, and articles about historical novels from various time periods."
    ),
    Website(
        genreName="Non Fiction",
        Title = "Medium",
        Link = "https://medium.com/",
        Description = "A platform that hosts a wide range of non-fiction articles and essays on various topics, written by both professional writers and everyday users."
    ),
    Website(
        genreName="Non Fiction",
        Title = "TED Talks",
        Link = "https://www.ted.com/talks",
        Description = "Offers a vast collection of talks by experts in various fields, covering topics ranging from science and technology to personal development and social issues."
    ),   Website(
        genreName="Non Fiction",
        Title = "Brain Pickings",
        Link = "https://www.brainpickings.org/",
        Description = "Curates and shares insightful essays and articles about literature, art, science, philosophy, and more, often exploring the intersections between different disciplines."
    ),  Website(
        genreName="Non Fiction",
        Title = "The Atlantic",
        Link = "https://www.theatlantic.com/",
        Description = "Publishes in-depth articles, essays, and analysis on politics, culture, technology, and other current affairs topics."
    ),
    Website(
        genreName="Non Fiction",
        Title = "Nautilus",
        Link = "http://nautil.us/",
        Description = "Explores science, philosophy, and culture through long-form essays and articles that aim to provide a deeper understanding of the world around us."
    ),
    Website(
        genreName="Non Fiction",
        Title = "Aeon",
        Link = "https://aeon.co/",
        Description = "Features thought-provoking essays and articles on philosophy, science, psychology, culture, and society, offering unique perspectives and insights."
    ),
    Website(
        genreName="Non Fiction",
        Title = "The New Yorke",
        Link = "https://www.newyorker.com/",
        Description = "Publishes a wide range of non-fiction articles, essays, and commentary on politics, culture, arts, and current events."
    ),
    Website(
        genreName="Romantic",
        Title = "National Geographic",
        Link = "https://www.nationalgeographic.com/magazine/",
        Description = " Offers articles, photo essays, and multimedia content covering science, nature, history, culture, and exploration."
    ),
    Website(
        genreName="Romantic",
        Title = "Smithsonian Magazine",
        Link = "https://www.smithsonianmag.com/",
        Description = "Features articles and essays on history, science, arts, and culture, often with a focus on the Smithsonian Institution's collections and research."
    ),
    Website(
        genreName="Romantic",
        Title = "The Conversation",
        Link = "https://theconversation.com/",
        Description = "Publishes articles written by academics and experts on various topics, providing accessible insights and analysis on current affairs, science, technology, and more."
    ),
    Website(
        genreName="Romantic",
        Title = "Wattpad",
        Link = "https://www.wattpad.com/",
        Description = "Wattpad: Your gateway to a world of limitless stories, where imagination knows no bounds"
    ),
    Website(
        genreName="Romantic",
        Title = "Romance.io",
        Link = "https://www.romance.io/",
        Description = " A website dedicated to romance fiction, offering book recommendations, author interviews, and deals on romance eBooks."
    ),
    Website(
        genreName="Romantic",
        Title = "Harlequin.com",
        Link = "https://www.harlequin.com/",
        Description = "The official website of Harlequin, a leading publisher of romance novels. It offers a wide selection of romance eBooks for purchase, including various subgenres like contemporary, historical, and paranormal romance."
    ),
    Website(
        genreName="Romantic",
        Title = "The Ripped Bodice",
        Link = "https://www.therippedbodicela.com/",
        Description = "A romance bookstore that also provides online ordering for romance novels in various subgenres."
    ),
    Website(
        genreName="Romantic",
        Title = "Romance Junkies",
        Link = "https://www.romancejunkies.com/",
        Description = "Features reviews, author interviews, and articles about romance novels, helping readers discover new romantic reads."
    ),
    Website(
        genreName="Romantic",
        Title = "Smart Bitches, Trashy Books",
        Link = "https://smartbitchestrashybooks.com/",
        Description = "A blog and podcast that reviews and discusses romance novels, offering recommendations and humorous insights."
    ),
    Website(
        genreName="Romantic",
        Title = "All About Romance",
        Link = "https://allaboutromance.com/",
        Description = "Provides reviews, articles, and features about romance novels, including recommendations and discussion forums."
    ),
    Website(
        genreName="Romantic",
        Title = "Romantic Shorts",
        Link = "https://romanticshorts.com/",
        Description = "Offers short romantic fiction stories for free reading online, catering to readers who enjoy quick romantic escapes."
    ),
    Website(
        genreName="Romantic",
        Title = "Romance Daily News",
        Link = "https://romancedailynews.com/",
        Description = "Provides news, reviews, and features about romance novels, helping readers stay up-to-date with the latest releases and trends in the genre."
    ),
    Website(
        genreName="Biography",
        Title = "Biography.com",
        Link = "https://www.biography.com/",
        Description = "Offers a vast collection of biographies covering historical figures, celebrities, and notable personalities from various fields."
    ),
    Website(
        genreName="Biography",
        Title = "Goodreads",
        Link = "https://www.goodreads.com/",
        Description = "Provides a section dedicated to biographies and memoirs, where you can find lists of popular biographical books, reader reviews, and recommendations."
    ),
    Website(
        genreName="Biography",
        Title = "Biography Online",
        Link = "https://www.biographyonline.net/",
        Description = "Features biographies of famous people from history, literature, science, and other fields, providing insightful profiles and articles."
    ),
    Website(
        genreName="Biography",
        Title = "The Famous People",
        Link = "https://www.thefamouspeople.com/",
        Description = "Offers biographies of famous personalities from various domains, including historical figures, celebrities, and leaders."
    ),
    Website(
        genreName="Biography",
        Title = "American National Biography Online",
        Link = "https://www.anb.org/",
        Description = "A comprehensive biographical database of significant figures in American history, culture, and society."
    ),
    Website(
        genreName="Biography",
        Title = "Scholastic",
        Link = "https://www.scholastic.com/",
        Description = "Offers a wide range of children's books, educational materials, and resources for parents, teachers, and kids."
    ),
    Website(
        genreName="Biography",
        Title = "Storyline Online",
        Link = "https://www.storylineonline.net/",
        Description = "Features videos of famous actors reading children's books aloud, accompanied by illustrations."
    ),
    Website(
        genreName="Biography",
        Title = "Epic!",
        Link = "https://www.getepic.com/",
        Description = "Offers a wide range of children's books, educational materials, and resources for parents, teachers, and kids."
    ),
    Website(
        genreName="Biography",
        Title = "Reading Rockets",
        Link = "https://www.readingrockets.org/",
        Description = "Provides resources and activities to help young readers, including book recommendations, reading strategies, and literacy tips for parents and educators."
    ),
    Website(
        genreName="Biography",
        Title = "Kidsreads",
        Link = "https://www.kidsreads.com/",
        Description = " Features book reviews, author interviews, and reading guides for children's and young adult literature."
    ),
    Website(
        genreName="Biography",
        Title = "Children's Book Council",
        Link = "https://www.cbcbooks.org/",
        Description = "A trade association for children's book publishers, offering resources, awards, and events related to children's literature."
    ),
    Website(
        genreName="Biography",
        Title = "The Horn Book",
        Link = "https://www.hbook.com/",
        Description = "Publishes reviews, articles, and features about children's and young adult literature, including recommended reading lists and insights from industry professionals."
    ),
    Website(
        genreName="Biography",
        Title = "Association for Library Service to Children (ALSC)",
        Link = "https://www.ala.org/alsc/",
        Description = "Provides resources, awards, and professional development opportunities for librarians and educators who work with children and young adults."
    ),
    Website(
        genreName="Biography",
        Title = "Brightly",
        Link = "https://www.readbrightly.com/",
        Description = "Offers book recommendations, reading tips, and activities to promote literacy and a love of reading in children."
    ),
    Website(
        genreName="Thriller",
        Title = "The Thrill Begins",
        Link = "https://thrillerwriters.org/",
        Description = "The official website of the International Thriller Writers organization, featuring articles, interviews, and resources for thriller writers and fans."
    ),
    Website(
        genreName="Thriller",
        Title = "CrimeReads",
        Link = "https://crimereads.com/",
        Description = "Offers articles, essays, and features about crime fiction, including thrillers, mysteries, and true crime stories."
    ),
    Website(
        genreName="Thriller",
        Title = "The Big Thrill",
        Link = "https://www.thebigthrill.org/",
        Description = "The online magazine of the International Thriller Writers organization, featuring author interviews, book reviews, and news about the thriller genre."
    ),
    Website(
        genreName="Thriller",
        Title = "BookBub",
        Link = "https://www.bookbub.com/",
        Description = "Offers deals on thriller and suspense eBooks, including discounted and free titles, tailored to your reading preferences"
    ),
    Website(
        Title = "Killer Nashville",
        Link = "https://killernashville.com/",
        Description = "Provides resources, articles, and events for mystery, thriller, and crime fiction writers and readers."
    ),
    Website(
        genreName="Thriller",
        Title = "Crime Fiction Lover",
        Link = "https://crimefictionlover.com/",
        Description = "Features reviews, articles, and news about crime fiction, including thrillers, mysteries, and noir novels."
    ),
    Website(
        genreName="Thriller",
        Title = "Dead Good",
        Link = "https://www.deadgoodbooks.co.uk/",
        Description = "Offers book recommendations, author interviews, and reading lists for fans of crime and thriller fiction."
    ),
    Website(
        genreName="Thriller",
        Title = "Mystery Tribune",
        Link = "https://www.mysterytribune.com/",
        Description = "Features reviews, interviews, and articles about mystery, thriller, and suspense fiction from around the world."
    ),
    Website(
        genreName="Thriller",
        Title = "The Real Book Spy",
        Link = "https://therealbookspy.com/",
        Description = " Reviews and news about the latest thriller and espionage novels, as well as author interviews and reading recommendations."
    ),
    Website(
        genreName="Thriller",
        Title = "International Thriller Writers",
        Link = "https://thrillerwriters.org/",
        Description = " The official website of the ITW organization, providing resources, events, and networking opportunities for thriller writers and fans."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Tor.com",
        Link = "https://www.tor.com/",
        Description = "Features original science fiction and fantasy short stories, as well as articles, reviews, and discussions about the genre."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Clarkesworld Magazine",
        Link = "https://clarkesworldmagazine.com/",
        Description = "Publishes science fiction and fantasy short stories, essays, and interviews with authors."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Lightspeed Magazine",
        Link = "https://www.lightspeedmagazine.com/",
        Description = " Offers a mix of science fiction and fantasy short stories, author interviews, and podcasts."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Escape Pod",
        Link = "https://escapepod.org/",
        Description = " A science fiction podcast that features weekly audio productions of short stories from new and established authors."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "io9",
        Link = "https://gizmodo.com/io9",
        Description = "A blog that covers science fiction, fantasy, and other genres of speculative fiction, featuring news, reviews, and articles about books, movies, and TV shows."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "SFWA (Science Fiction and Fantasy Writers of America)",
        Link = "https://www.sfwa.org/",
        Description = "The official website of the SFWA organization, offering resources for writers, including market listings, writing tips, and information about the genre."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Baen Books",
        Link = "https://www.baen.com/",
        Description = "A publisher of science fiction and fantasy novels, offering some free ebooks and a monthly online magazine, Baen's Universe."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "The Best Science Fiction Books",
        Link = "https://best-sci-fi-books.com/",
        Description = "Provides book recommendations, reviews, and lists of the best science fiction books across various subgenres."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Science Fiction and Fantasy Writers of America (SFWA) Blog",
        Link = "",
        Description = "SFWA Blog - Features articles, essays, and insights from science fiction and fantasy authors and industry professionals."
    ),
    Website(
        genreName="Sci-Fi",
        Title = "Tangent Online",
        Link = "https://tangentonline.com/",
        Description = " Reviews short fiction in the science fiction, fantasy, and horror genres, providing analysis and recommendations for readers."
    ),
    Website(
        genreName="Mystery",
        Title = "CrimeReads",
        Link = "https://crimereads.com/",
        Description = "Offers articles, essays, and features about crime fiction, including mysteries, thrillers, and true crime stories."
    ),
    Website(
        genreName="Mystery",
        Title = "The Strand Magazine",
        Link = "https://strandmag.com/",
        Description = "Features short stories, interviews, and reviews focused on mystery and detective fiction."
    ),
    Website(
        genreName="Mystery",
        Title = "The Mystery Reader",
        Link = "https://www.themysteryreader.com/",
        Description = "Reviews and recommendations of mystery novels and authors, including cozy mysteries, police procedurals, and psychological thrillers."
    ),
    Website(
        genreName="Mystery",
        Title = "Cozy Mystery List Blog",
        Link = "https://cozy-mystery.com/blog/",
        Description = "Features articles, news, and updates about cozy mysteries, a subgenre of mystery fiction known for its light-hearted and amateur detective protagonists."
    ),
    Website(
        genreName="Mystery",
        Title = "Kirkus Reviews",
        Link = "https://www.kirkusreviews.com/",
        Description = "Mystery & Thriller - Offers reviews and recommendations of mystery and thriller novels from Kirkus Reviews, a respected book review publication."
    ),
    Website(
        genreName="Mystery",
        Title = "Stop, You're Killing Me!",
        Link = "https://www.stopyourekillingme.com/",
        Description = "A resource for mystery, crime, thriller, spy, and suspense fiction, providing lists of books, authors, and series organized by subgenre, location, and time period."
    ),
    Website(
        genreName="Mystery",
        Title = "Mystery Scene Magazine",
        Link = "https://www.mysteryscenemag.com/",
        Description = "Features articles, interviews, and reviews about mystery, crime, and suspense fiction, as well as updates about the genre and its authors."
    ),
    Website(
        genreName="Mystery",
        Title = "Criminal Element",
        Link = "https://www.criminalelement.com/",
        Description = "Offers articles, reviews, and features about crime fiction, including mystery novels, thrillers, and true crime stories."
    ),
    Website(
        genreName="Mystery",
        Title = "The Big Thrill",
        Link = "https://www.thebigthrill.org/",
        Description = "The online magazine of the International Thriller Writers organization, featuring author interviews, book reviews, and news about mystery and thriller fiction."
    ),
    Website(
        genreName="Mystery",
        Title = "Poisoned Pen Press Blog",
        Link = "https://poisonedpen.com/blog/",
        Description = "Features articles, interviews, and updates from Poisoned Pen Press, a publisher specializing in mystery and crime fiction."
    ),
    Website(
        genreName="History",
        Title = "History.com",
        Link = "https://www.history.com/",
        Description = "The official website of the History Channel, offering articles, videos, and interactive features about historical events, people, and topics."
    ),
    Website(
        genreName="History",
        Title = "BBC History",
        Link = "https://www.bbc.co.uk/history/british/",
        Description = "Provides articles, podcasts, and interactive timelines covering a wide range of historical periods, events, and figures."
    ),
    Website(
        genreName="History",
        Title = "National Geographic History",
        Link = "https://www.nationalgeographic.com/magazine/national-geographic-history/",
        Description = "Offers articles, photos, and features about ancient civilizations, world history, and archaeological discoveries."
    ),
    Website(
        genreName="History",
        Title = "Smithsonian Magazine",
        Link = "https://www.smithsonianmag.com/",
        Description = "Features articles, essays, and multimedia content about history, science, culture, and exploration."
    ),
    Website(
        genreName="History",
        Title = "History Extra",
        Link = "https://www.historyextra.com/",
        Description = "The website of BBC History Magazine, offering articles, podcasts, and quizzes about historical events and figures."
    ),
    Website(
        genreName="History",
        Title = "The History Net",
        Link = "https://www.historynet.com/",
        Description = "Features articles, blogs, and forums about military history, world history, and historical topics."
    ),
    Website(
        genreName="History",
        Title = "Encyclopedia Britannica",
        Link = "https://www.britannica.com/",
        Description = "Offers authoritative articles and resources covering a wide range of historical topics, as well as other subjects."
    ),
    Website(
        genreName="History",
        Title = "American Historical Association",
    Link = "https://www.historians.org/",
    Description = "Provides resources, publications, and news about historical research, teaching, and scholarship."
    ),
    Website(
        genreName="History",
        Title = "History Today",
        Link = "https://www.historytoday.com/",
        Description = "Features articles, podcasts, and reviews about historical events, figures, and debates."
    ),
    Website(
        genreName="History",
        Title = "Digital Public Library of America (DPLA)",
        Link = "https://dp.la/",
        Description = "Offers access to millions of digital resources from libraries, archives, and museums across the United States, including historical photographs, documents, and artifacts."
    ),
    Website(
        genreName="Poetry",
        Title = "Poetry Foundation",
        Link = "https://www.poetryfoundation.org/",
        Description = "Offers a vast collection of poems, articles, podcasts, and educational resources about poetry."
    ),
    Website(
        genreName="Poetry",
        Title = "Poets.org",
        Link = "https://poets.org/",
        Description = "The website of the Academy of American Poets, featuring thousands of poems, essays, biographies of poets, and information about poetry events and awards."
    ),
    Website(
        genreName="Poetry",
        Title = "The Poetry Archive",
        Link = "https://www.poetryarchive.org/",
        Description = "A collection of recordings of poets reading their own work, along with biographical information and commentary.The Paris Review - Poetry: The Paris Review - Poetry - Features contemporary poetry, interviews with poets, and essays about poetry and poetics."
    ),
    Website(
        genreName="Poetry",
        Title = "Button Poetry",
        Link = "https://buttonpoetry.com/",
        Description = "A platform for spoken word and performance poetry, featuring videos, audio recordings, and written poems by a diverse range of poets."
    ),
    Website(
        genreName="Poetry",
        Title = "Poetry Magazine",
        Link = "https://www.poetryfoundation.org/poetrymagazine",
        Description = "The website of Poetry Foundation's flagship publication, offering a wide selection of contemporary poetry, reviews, and essays."
    ),
    Website(
        genreName="Poetry",
        Title = "The Poetry Society",
        Link = "https://poetrysociety.org.uk/",
        Description = "Provides information about poetry competitions, events, and publications, as well as resources for poets and poetry enthusiasts."
    ),
    Website(
        genreName="Poetry",
        Title = "Poetry Daily",
        Link = "https://poems.com/",
        Description = "Publishes a new poem every day from contemporary poets, along with featured poems, interviews, and reviews."
    ),
    Website(
        genreName="Poetry",
        Title = "The Rumpus - Poetry",
        Link = "https://therumpus.net/topics/poetry/",
        Description = "Features poetry from emerging and established poets, as well as interviews, reviews, and essays about poetry."
    ),
    Website(
        genreName="Poetry",
        Title = "Academy of American Poets",
        Link = "https://poets.org/",
        Description = " Delivers a new poem to your inbox every day, along with commentary and analysis from poets and critics."
    ),
    Website(
        genreName="Horror",
        Title = "Nightmare Magazine",
        Link = "https://www.nightmare-magazine.com/",
        Description = "Publishes horror and dark fantasy fiction, as well as author interviews, articles, and reviews."
    ),
    Website(
        genreName="Horror",
        Title = "Cemetery Dance",
        Link = "https://www.cemeterydance.com/",
        Description = "Offers horror fiction, news, and reviews, as well as a print magazine specializing in horror literature."
    ),
    Website(
        genreName="Horror",
        Title = "Horror Writers Association (HWA)",
        Link = "https://horror.org/",
        Description = "Provides resources for horror writers and fans, including articles, events, and information about the horror genre."
    ),
    Website(
        genreName="Horror",
        Title = "Creepypasta",
        Link = "https://www.creepypasta.com/",
        Description = "A collection of user-generated horror stories and urban legends, often presented in a creepypasta format for online sharing."
    ),
    Website(
        genreName="Horror",
        Title = "Reddit - r/NoSleep",
        Link = "https://www.reddit.com/r/nosleep/",
        Description = " A subreddit for original horror stories written in a narrative format, often presented as if they are true experiences."
    ),
    Website(
        genreName="Horror",
        Title = "Horror Bound",
        Link = "https://horrorbound.net/",
        Description = "Features horror book reviews, author interviews, and articles about horror literature and film."
    ),
    Website(
        genreName="Horror",
        Title = "Ginger Nuts of Horro",
        Link = "http://gingernutsofhorror.com/",
        Description = "Offers horror fiction reviews, interviews with authors, and articles about horror culture and fandom."
    ),
    Website(
        genreName="Horror",
        Title = "The Dark Magazine",
        Link = "https://thedarkmagazine.com/",
        Description = "Publishes horror and dark fantasy fiction, as well as occasional non-fiction articles and reviews."
    ),

    Website(
        genreName="Horror",
        Title = "Daily Dead",
        Link = "https://dailydead.com/",
        Description = "Offers horror news, reviews, and features about horror movies, TV shows, books, and games."
    ),
    Website(
        genreName="Horror",
        Title = "Tor Nightfire",
        Link = "https://www.tornightfire.com/",
        Description = "Features horror fiction, articles, and news from Tor Books, a publisher specializing in speculative fiction."
    ),
    Website(
        genreName="Crime",
        Title = "CrimeReads",
        Link = "https://crimereads.com/",
        Description = "Offers articles, essays, and features about crime fiction, including mysteries, thrillers, and true crime stories."
    ),
    Website(
        genreName="Crime",
        Title = "The Rap Sheet",
        Link = "https://therapsheet.blogspot.com/",
        Description = "A blog that covers crime fiction news, reviews, and interviews with authors and industry professionals."
    ),
    Website(
        genreName="Crime",
        Title = "The Strand Magazine",
        Link = "https://strandmag.com/",
        Description = "Features short stories, interviews, and reviews focused on mystery and detective fiction."
    ),
    Website(
        genreName="Crime",
        Title = "Mystery Scene Magazine",
        Link = "https://www.mysteryscenemag.com/",
        Description = "Features articles, interviews, and reviews about mystery, crime, and suspense fiction."
    ),
    Website(
        genreName="Crime",
        Title = "Stop, You're Killing Me!:",
        Link = "https://www.stopyourekillingme.com/",
        Description = "A resource for mystery, crime, thriller, spy, and suspense fiction, providing lists of books, authors, and series organized by subgenre, location, and time period."
    ),
    Website(
        genreName="Crime",
        Title = "Kirkus Reviews",
        Link = "https://www.kirkusreviews.com/",
        Description = "Offers reviews and recommendations of mystery and thriller novels from Kirkus Reviews, a respected book review publication."
    ),
    Website(
        genreName="Crime",
        Title = "Criminal Element",
        Link = "https://www.criminalelement.com/",
        Description = "Offers articles, reviews, and features about crime fiction, including mystery novels, thrillers, and true crime stories."
    ),
    Website(
        genreName="Crime",
        Title = "The Big Thrill",
        Link = "https://www.thebigthrill.org/",
        Description = "The online magazine of the International Thriller Writers organization, featuring author interviews, book reviews, and news about mystery and thriller fiction."
    ),
    Website(
        genreName="Crime",
        Title = "Poisoned Pen Press Blog",
        Link = "https://poisonedpen.com/blog/",
        Description = "Features articles, interviews, and updates from Poisoned Pen Press, a publisher specializing in mystery and crime fiction."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Crime Fiction Lover",
        Link = "https://crimefictionlover.com/",
        Description = "Features reviews, articles, and news about crime fiction, including mystery novels, thrillers, and noir."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Goodreads",
        Link = "https://www.goodreads.com/",
        Description = "Provides a section dedicated to autobiographies and memoirs, where you can find lists of popular titles, reader reviews, and recommendations."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "BookBrowse",
        Link = "https://www.bookbrowse.com/",
        Description = " Offers book reviews, excerpts, and author biographies for autobiographies and memoirs, helping readers discover new and noteworthy titles."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Oprah's Book Club",
        Link = "https://www.oprahsbookclub.com/",
        Description = "Features Oprah Winfrey's selections for her book club, including autobiographical works, along with discussions and reading guides."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Biography.com",
        Link = "https://www.biography.com/",
        Description = "Offers articles, videos, and features about notable individuals and their autobiographical works."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "The New York Times - Books",
        Link = "https://www.nytimes.com/section/books",
        Description = "Provides reviews, interviews, and articles about autobiographies and memoirs, as well as other literary genres."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Memoir Writer's Journey",
        Link = "https://www.memoirwritersjourney.com/",
        Description = "Offers resources, tips, and inspiration for memoir writers, including author interviews and writing prompts."
    ),
    Website(
        genreName="Auto-Biography",
            Title = "BookPage",
    Link = "https://www.bookpage.com/",
    Description = "Features reviews, author interviews, and articles about autobiographies and memoirs, helping readers discover new and notable releases."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "The Guardian - Books",
        Link = "https://www.theguardian.com/books/autobiography",
        Description = " Offers reviews, features, and interviews about autobiographies and memoirs, along with other literary content."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Literary Hub - Memoir",
        Link = "https://www.bing.com/ck/a?!&&p=312f657a1105712fJmltdHM9MTcxMzA1MjgwMCZpZ3VpZD0yZjRhNjllNi1kOGFkLTZkNzYtM2U1Mi03YThlZDljMjZjMzMmaW5zaWQ9NTIxOQ&ptn=3&ver=2&hsh=3&fclid=2f4a69e6-d8ad-6d76-3e52-7a8ed9c26c33&psq=Literary+Hub+-+Memoir&u=a1aHR0cHM6Ly9saXRodWIuY29tL2NhdGVnb3J5L25ld3NhbmRjdWx0dXJlL21lbW9pci8&ntb=1",
        Description = "Features essays, excerpts, and interviews about memoirs and autobiographies, as well as other literary topics."
    ),
    Website(
        genreName="Auto-Biography",
        Title = "Writer's Digest",
        Link = "https://www.writersdigest.com/",
        Description = "Provides tips, articles, and resources for memoir writers, including advice from publishing professionals and successful authors."
    ),
    Website(
        genreName="CookBook",
        Title = "Epicurious",
        Link = "https://www.epicurious.com/",
        Description = "Offers a vast collection of recipes, cooking tips, and articles from renowned chefs and food experts."
    ),
    Website(
        genreName ="CookBook",
        Title = "Allrecipes",
        Link = "https://www.allrecipes.com/",
        Description = "A popular platform where users can find and share recipes, cooking tips, and reviews."
    ),
    Website(
        genreName="CookBook",
        Title = "Food Network",
        Link = "https://www.foodnetwork.com/",
        Description = "Features recipes, cooking videos, and culinary shows from celebrity chefs and food personalities."
    ),
    Website(
        genreName="CookBook",
            Title = "BBC Good Food",
    Link = "https://www.bbcgoodfood.com/",
    Description = "Offers recipes, cooking tips, and meal ideas from the British Broadcasting Corporation (BBC)."
    ),
    Website(
        genreName="CookBook",
        Title = "Taste of Home",
        Link = "https://www.tasteofhome.com/",
        Description = "Provides recipes, cooking videos, and culinary inspiration for home cooks."
    ),
    Website(
        genreName="CookBook",
        Title = "Serious Eats",
        Link = "https://www.seriouseats.com/",
        Description = " Features recipes, cooking techniques, and food science articles from professional chefs and food writers."
    ),
    Website(
        genreName="CookBook",
        Title = "Bon Appétit",
        Link = "https://www.bonappetit.com/",
        Description = "Offers recipes, cooking tips, and food culture articles from the popular culinary magazine."
    ),
    Website(
        genreName="CookBook",
        Title = "The Kitchn",
        Link = "https://www.thekitchn.com/",
        Description = " Provides recipes, cooking tutorials, and kitchen tips for home cooks."
    ),
    Website(
        genreName="CookBook",
        Title = "Delish",
        Link = "https://www.delish.com/",
        Description = "Offers recipes, cooking videos, and food trends from the popular food and lifestyle website."
    ),
    Website(
        genreName="CookBook",
        Title = "Cooking Light",
        Link = "https://www.cookinglight.com/",
        Description = "Features healthy recipes, cooking techniques, and meal plans for a balanced lifestyle."
    ),
    Website(
        genreName="Coding",
        Title = "Codecademy",
        Link = "https://www.codecademy.com/",
        Description = " Offers interactive coding lessons in various programming languages such as Python, JavaScript, HTML/CSS, and more."
    ),
    Website(
        genreName="Coding",
        Title = "freeCodeCamp",
        Link = "https://www.freecodecamp.org/",
        Description = "Provides free coding tutorials and projects in web development, covering HTML, CSS, JavaScript, and more."
    ),
    Website(
        genreName="Coding",
        Title = "LeetCode",
        Link = "https://leetcode.com/",
        Description = "Focuses on coding challenges and algorithm questions, ideal for those preparing for technical interviews or aiming to enhance problem-solving skills."
    ),
    Website(
        genreName="Coding",
        Title = "Khan Academy",
        Link = "https://www.khanacademy.org/",
        Description = "Offers beginner-friendly coding courses covering topics such as HTML, CSS, JavaScript, and computer science fundamentals."
    ),
    Website(
        genreName="Coding",
        Title = "Coursera",
        Link = "https://www.coursera.org/",
        Description = " Provides a wide range of coding courses from universities and institutions around the world, including both free and paid options."
    ),
    Website(
        genreName="Coding",
        Title = "edX",
        Link = "https://www.edx.org/",
        Description = " Similar to Coursera, edX offers coding courses from universities and institutions, often including certificates upon completion."
    ),
    Website(
        genreName="Coding",
        Title = "Udemy",
        Link = "https://www.udemy.com/",
        Description = "Features a vast collection of coding courses on various topics, ranging from beginner to advanced levels, often at affordable prices."
    ),
    Website(
        genreName="Coding",
        Title = "Stack Overflow",
        Link = "https://stackoverflow.com/",
        Description = "While primarily a question and answer site for programmers, it's an invaluable resource for troubleshooting coding issues and learning from the experiences of others."
    ),
    Website(
        genreName="Coding",
        Title = "GitHub ",
        Link = "https://github.com/",
        Description = " Besides being a platform for version control and collaboration, GitHub hosts a multitude of open-source projects that you can explore and contribute to, aiding in learning through practical application."
    ),
    Website(
        genreName ="Coding",
        Title = "HackerRank",
        Link = "https://www.hackerrank.com/",
        Description = "Offers coding challenges, competitions, and interview preparation materials, enabling users to practice and improve their coding skills."
    ),
    Website(
        Title = "",
        Link = "",
        Description = ""
    ),
)


val genres= listOf(
    Genre("Coding"),
    Genre("CookBook"),
    Genre("Auto-Biography"),
    Genre("Crime"),
    Genre("Horror"),
    Genre("Poetry"),
    Genre("History"),
    Genre(" Mystery"),
    Genre("Sci - Fi"),
    Genre("Thriller"),
    Genre("Childrens Literature"),
    Genre("Biography"),
    Genre("Romantic"),
    Genre("Non Fiction"),
    Genre("Historical Fiction"),
    Genre("Narrative"),
    Genre("Novel"),
    Genre("Fiction")
)
