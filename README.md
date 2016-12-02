# Test Registration and Authentication System
This project started out as a way for me to test ideas on how to architect a
slightly more complex application than most conference talks give using purely
functional constructs like Reader Monad and Kleisli arrows.

The idea is to have a layered architecture with multiple domains talking to
a data access tier each with their own `environment` to operate in. Some
questions I was looking to answer is:

* How do you organize environment / config files.
    * One big env file passed around
    * One env per 'tier'
    * Local env for each system
* How do you organize a codebase that depends on other domains
* How do you deal with adding or removing domain layers from an existing system

This is a fork of my existing work which I am doing offline, so I don't expect a
whole lot of commits coming this way. This was just a way to get it up into
the public space so I can collect feedback and hopefully inspire other engineers
who are learning to keep moving foward.

