CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: BGRSclient

BGRSclient: bin/echoClient.o bin/connectionHandler.o bin/operation.o bin/protocol.o bin/serverListener.o
	@echo "Building target: BGRSclient"
	g++ -o bin/BGRSclient bin/echoClient.o bin/connectionHandler.o bin/operation.o bin/protocol.o bin/serverListener.o $(LDFLAGS)
	@echo "finished Linking"

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/operation.o: src/operation.cpp
	g++ $(CFLAGS) -o bin/operation.o src/operation.cpp

bin/protocol.o: src/protocol.cpp
	g++ $(CFLAGS) -o bin/protocol.o src/protocol.cpp

bin/serverListener.o: src/serverListener.cpp
	g++ $(CFLAGS) -o bin/serverListener.o src/serverListener.cpp

.PHONY: clean
clean:
	rm -f bin/*
