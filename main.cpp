#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <queue>
#include <random>
#include <mutex>
#include <condition_variable>
#include <thread>
#include <chrono>
#include <iomanip>

// ITstudent class to store student information
class ITstudent
{
  public:
    std::string name;
    std::string id;
    std::string programme;
    std::vector<std::pair<std::string, int> >
        courses; // Course name and associated mark

    void calculateAverage()
    {
        int totalMarks = 0;
        for (const auto &course : courses) {
            totalMarks += course.second;
        }
        averageMark = totalMarks / courses.size();
    }

    void determinePassFail()
    {
        passFail = (averageMark >= 50) ? "Pass" : "Fail";
    }

    void printInfo()
	{
		std::cout << "=====================================================================" << std::endl;
		std::cout <<std::setw(20) <<std::left << "Student Name: " << name << std::endl;
		std::cout <<std::setw(20) <<std::left << "Student ID: " << id << std::endl;
		std::cout <<std::setw(20) <<std::left << "Programme: " << programme << std::endl;
		std::cout << "---------------------------------------------------------------------" << std::endl;
		std::cout <<std::setw(20) <<std::left << "Courses and Marks:" << std::endl;
		for (const auto &course : courses) {
			std::cout <<std::setw(55) <<std::left << course.first << ": " <<std::setw(7) <<std::right << course.second << std::endl;
		}
		std::cout << "---------------------------------------------------------------------" << std::endl;
		std::cout <<std::setw(55) <<std::left << "Average Mark "<< ": " <<std::setw(7) <<std::right << averageMark << std::endl;
		std::cout << "---------------------------------------------------------------------" << std::endl;
		std::cout <<std::setw(20) <<std::left << "Pass/Fail: " << passFail << std::endl;
		std::cout << "=====================================================================" << std::endl;
		std::cout << std::endl;
        std::cout << std::endl;
    }
  private:
    int averageMark;
    std::string passFail;
};

// Buffer class to store shared data and handle synchronization
class Buffer
{
  public:
    Buffer(int maxSize) : maxSize(maxSize) {}

    void produce(int fileNumber)
    {
        std::unique_lock<std::mutex> lock(mutex);
        cvProduce.wait(lock, [this]() { return buffer.size() < maxSize; });

        std::string fileName = "student" + std::to_string(fileNumber) + ".xml";
        std::ofstream file(fileName); // Create XML file

        ITstudent student;
        generateStudentInfo(student); // Generate random student information

        // Write student information to XML file
        file << "<Student>" << std::endl;
        file << "\t<Name>" << student.name << "</Name>" << std::endl;
        file << "\t<ID>" << student.id << "</ID>" << std::endl;
        file << "\t<Programme>" << student.programme << "</Programme>"
             << std::endl;
        file << "\t<Courses>" << std::endl;
		for (const auto &course : student.courses) {
            file << "\t\t<Course>" << std::endl;
            file << "\t\t\t<Name>" << course.first << "</Name>" << std::endl;
            file << "\t\t\t<Mark>" << course.second << "</Mark>" << std::endl;
            file << "\t\t</Course>" << std::endl;
        }
        file << "\t</Courses>" << std::endl;
        file << "</Student>" << std::endl;

        file.close();

        buffer.push(fileNumber);
        cvConsume.notify_all();
    }

    void consume()
    {
        std::unique_lock<std::mutex> lock(mutex);
        cvConsume.wait(lock, [this]() { return !buffer.empty(); });

        int fileNumber = buffer.front();
        buffer.pop();

        std::string fileName = "student" + std::to_string(fileNumber) + ".xml";
        std::ifstream file(fileName); // Read XML file

        ITstudent student;
        parseStudentInfo(
            file, student); // Parse XML file and store student information

        file.close();
        std::remove(fileName.c_str()); // Delete XML file

        student.calculateAverage();
        student.determinePassFail();
        student.printInfo();

        cvProduce.notify_all();
    }
  private:
    int maxSize;
    std::queue<int> buffer;
    std::mutex mutex;
    std::condition_variable cvProduce;
    std::condition_variable cvConsume;

    void generateStudentInfo(ITstudent &student)
    {
        static std::random_device rd;
        static std::mt19937 gen(rd());
        static std::uniform_int_distribution<int> idDist(10000000, 99999999);
		static std::uniform_int_distribution<int> markDist(30, 80);
		static std::uniform_int_distribution<int> courseDist(3, 7);

		student.name = generateStudentName();
        student.id = std::to_string(idDist(gen));
		student.programme = generateProgrammeName();

        int numCourses = courseDist(gen); // Number of courses per student
        for (int i = 0; i < numCourses; i++) {
            std::string courseName = generateCourseName();
            int mark = markDist(gen);
            student.courses.push_back(std::make_pair(courseName, mark));
        }
    }

    void parseStudentInfo(std::ifstream &file, ITstudent &student)
    {
        std::string line;
        while (std::getline(file, line)) {
            if (line.find("<Name>") != std::string::npos) {
                student.name = parseValue(line);
            } else if (line.find("<ID>") != std::string::npos) {
                student.id = parseValue(line);
            } else if (line.find("<Programme>") != std::string::npos) {
                student.programme = parseValue(line);
            } else if (line.find("<Course>") != std::string::npos) {
                std::string courseName;
                int mark;

                while (std::getline(file, line) &&
                       line.find("</Course>") == std::string::npos) {
                    if (line.find("<Name>") != std::string::npos) {
                        courseName = parseValue(line);
                    } else if (line.find("<Mark>") != std::string::npos) {
                        mark = std::stoi(parseValue(line));
                    }
                }

                student.courses.push_back(std::make_pair(courseName, mark));
            }
        }
    }

	std::string generateStudentName()
    {
		std::string array[] = { "Mndzebele Sanele", "Fakudze Samkeliso"
		, "Dlamini Sibusile", "Matse Alibongwe", "Cele Cebile",
		"Dube Awakhe", "Vilakati Thembumenzi", "Adams Jason",
		"Mndzebele Siphelele", "Mkhonta Mphilo", "Fakudze Zethu",
		"Khumalo Walter", "Nhleko Zethu", "Mavuso Sanele",
		"Matsenjwa Promise", "Mabuza Sinaye", "Gamedze Lindokuhle"};

        // Get the size of the array
		int size = sizeof(array) / sizeof(array[0]);

        // Seed the random number generator
        std::random_device rd;
        std::mt19937 gen(rd());

        // Generate a random index
        std::uniform_int_distribution<int> dist(0, size - 1);
        int randomIndex = dist(gen);

        // Get the random value from the array
		std::string randomValue = array[randomIndex];

		return randomValue;
    }

	std::string generateProgrammeName()
    {
		std::string array[] = { "BSC IT - IDE", "BSC IT"
		, "BSC COMPUTER SCIENCE", "BSC INFORMATION SCIENCE", "BSC COMPUTER SCIENCE EDUCATION",
		"BSC COMPUTER SCIENCE EDUCATION IDE", "BSC GIS"};

        // Get the size of the array
		int size = sizeof(array) / sizeof(array[0]);

        // Seed the random number generator
        std::random_device rd;
        std::mt19937 gen(rd());

        // Generate a random index
        std::uniform_int_distribution<int> dist(0, size - 1);
        int randomIndex = dist(gen);

        // Get the random value from the array
		std::string randomValue = array[randomIndex];

		return randomValue;
	}

	std::string generateCourseName()
    {
		std::string array[] = { "CSC411 Integrative Programming Languges", "CSC421 Systems Administration"
		, "MAT111 Trigonometry", "MAT 112 Calculus", "CSC311 Data Structures and Algorithms",
		"CSC213 Computer Programming", "CSC431 Computer Networks"};

        // Get the size of the array
		int size = sizeof(array) / sizeof(array[0]);

        // Seed the random number generator
        std::random_device rd;
        std::mt19937 gen(rd());

        // Generate a random index
        std::uniform_int_distribution<int> dist(0, size - 1);
        int randomIndex = dist(gen);

        // Get the random value from the array
		std::string randomValue = array[randomIndex];

		return randomValue;
	}

    std::string parseValue(const std::string &line)
    {
        std::size_t start = line.find('>') + 1;
        std::size_t end = line.find('<', start);
        return line.substr(start, end - start);
    }
};

int main()
{
    const int maxSize = 10;
    Buffer buffer(maxSize);

    std::thread producer([&buffer]() {
        for (int i = 1; i <= 10; i++) {
            buffer.produce(i);
            std::this_thread::sleep_for(
                std::chrono::milliseconds(500)); // Delay between producing data
        }
    });

    std::thread consumer([&buffer]() {
        for (int i = 1; i <= 10; i++) {
            buffer.consume();
            std::this_thread::sleep_for(std::chrono::milliseconds(
                1000)); // Delay between consuming data
        }
    });

    producer.join();
    consumer.join();
    system("pause");
    return 0;
}

