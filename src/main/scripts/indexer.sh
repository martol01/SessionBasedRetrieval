gARCHIVE_NAME="archive.warc"
OUTPUT_FOLDER="/Users/ralucamelon/Downloads/indri-5.0/outputIndex"

echo "Param file: $1"

FILES="$1/*.warc.gz"
for file in $FILES
do
	echo "Unzipping warc.gz file: $file"
	# extract parameter file in
	gunzip -c $file > $ARCHIVE_NAME
	echo "<parameters> \
	     <memory>400m</memory> \
	     <index>$OUTPUT_FOLDER</index> \
	     <stemmer> \
	       <name>krovetz</name> \
	     </stemmer> \
	     <corpus> \
	     <path>$ARCHIVE_NAME</path> \
	     <class>warc</class> \
	     </corpus> \
	     <field><name>title</name></field> \
	     <field><name>heading</name></field> \
	     <field><name>body</name></field> \
	     <metadata> \
  				<forward>url</forward> \
  				<backward>url</backward> \
			 </metadata> \
			</parameters>" > param_file.xml

	# Run the indexer on the newly unzipped warc file.
	echo "Indexing documents from warc file"
	/Users/ralucamelon/Downloads/indri-5.0/buildindex/IndriBuildIndex param_file.xml stopwords.xml

	# Cleaning up generated files
	echo "Finished indexing $file. Cleaning generated files"
	rm archive.warc
	rm param_file.xml
done

echo "All archives indexed successfully."