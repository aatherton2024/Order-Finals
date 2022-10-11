## Repository Contents
This repository contains the starter files for the lab scheduling lab.
The starter files contain fictional student schedules with the format of
```
student-name
class1
class2
class3
class4
student-name
class1
class2
class3
class4
...
```

Each student entry has exactly 5 lines: the student name followed by 4
courses (one per line).

## Usage
See main method comments for more details:

To print out slots and the course catalog alphabetized with exam slot #'s and
enrolled students:
$  java ExamScheduler < filename

To print out slots and the highest and lowest random solutions:
$  java ExamScheduler #tests < filename
Where #tests is a positive integer