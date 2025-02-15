Web crawler project for the Computer linguistics course in FMI Plovdiv.

Starts with provided URL and regular expression and crawls the web page and matches
content with the regex. The found matches are stored in MongoDB.

Matches other URLs in the HTML content of the web page and recursively 
run the above-mentioned algorithm on them again (using DFS).

Maximum number of pages to be crawled is configured.

There are 2 implementations - single and multithreaded. They can be switched by setting the 
`multithreaded` option to true or false.

The single-threaded implementation uses a Stack to push the newly found URLs
and runs an iterative DFS to crawl them. 

The multithreaded implementation uses BlockingQueue to push the URLs.
The main thread waits for URL to be added and submits a new task to the ExecutorService
to crawl the next URL. 

The data is stored in MongoDB as an object containing the URL that was crawled,
the regex that was provided and set of matches.

To run the project locally, you need to be able to connect to MongoDB cluster.
To check how to install MongoDB locally on Windows -> https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-windows/

