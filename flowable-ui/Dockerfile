# Use an official Tomcat runtime as a parent image 
FROM tomcat:8.0-jre8 
ENV DIRPATH /usr/local/tomcat 
# Copy Tomcat users into the container at /conf 
COPY ./tomcat-users.xml $DIRPATH/conf 
# Copy flowable-admin war file into the container at /webapps 
COPY ./flowable-ui-add-mysql.war $DIRPATH/webapps

# Make port 8080 available to the world outside this container 
EXPOSE 8080 
# Run CMD "catalina.sh" when the container launches 
CMD ["catalina.sh", "run"]