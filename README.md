# AutoGPT4Java
----------------------------------

## Short description
This repository is based on the implementation principles of the Python version of AutoGPT and is implemented in java to support multilingual switching of Prompt.

## Reference
2. https://github.com/Significant-Gravitas/Auto-GPT

## Configuration content
This is currently a test version, so the logic is written inside the test class, and the following environment variable (OPENAI_API_KEY) needs to be configured before it can be used.

There are still some problems with this version, to be solved later.

## Implemented commands
* append_to_file
* delete_file
* read_file
* search_files
* google_search
* browse_website
* task_complete

## with implemented functions

* Automatically calculate the requested token to prevent the token from exceeding the length
* Dynamically load commands based on their content
* Add execution of script commands (groovy)
