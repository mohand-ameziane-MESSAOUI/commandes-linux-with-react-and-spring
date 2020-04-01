FROM java:8

RUN apt-get update -y 
RUN	apt-get install -y  gcc
RUN apt-get install -y  graphviz
RUN apt install  -y --no-install-recommends texlive-latex-base
RUN apt-get install  -y --no-install-recommends texlive-luatex
RUN apt-get install  -y  texlive-xetex


WORKDIR /home



COPY ./target/commandsProject-0.0.1-SNAPSHOT.jar /home


ENTRYPOINT ["java","-jar","commandsProject-0.0.1-SNAPSHOT.jar"]  
