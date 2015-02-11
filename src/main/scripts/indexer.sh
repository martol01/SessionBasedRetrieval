#!/bin/bash

USAGE="Usage: ./indexer.sh input_directory index_table_of_content_file"
TEMP_ARCHIVE="archive.warc"
TEMP_PARAM_FILE="param_file.xml"
STOPWORDS_FILE="stopwords.xml"

# Usage validation: check that the script was passed 2 parameters, both directories
if [ "$#" -ne 2 ] || ! [ -d "$1" ]; then
    echo ${USAGE}
    exit 1
fi

INPUT_DIRECTORY=$1
INDEX_FILE_PATH=$2

echo "Folder containing archives: $INPUT_DIRECTORY"
echo "Indri home directory: $INDEX_FILE_PATH"

IFS=$'\t\n'
FILES=(`find "${INPUT_DIRECTORY}" -name *.warc.gz`)
for file in "${FILES[@]}"
do
	echo "Unzipping warc.gz file: $file"
	# extract parameter file in
	gunzip -c "${file}" > ${TEMP_ARCHIVE}
	echo $"<parameters> \
	     <memory>400m</memory> \
	     <index>${INDEX_FILE_PATH}</index> \
	     <stemmer> \
	       <name>krovetz</name> \
	     </stemmer> \
	     <corpus> \
	        <path>${TEMP_ARCHIVE}</path> \
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
	IndriBuildIndex ${TEMP_PARAM_FILE} ${STOPWORDS_FILE}

	# Cleaning up generated files
	echo "Finished indexing ${file}. Cleaning generated files"
	rm ${TEMP_ARCHIVE}
	rm ${TEMP_PARAM_FILE}
done

echo "All archives indexed successfully."
