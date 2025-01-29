# GameBot
An Android app to store a library of games (board, card, video, etc...) and allow the user to get a randomly selected game to play based on parameters they set, such as duration and how much space the game takes.

> ! NOTE ! This was initially built for a specific, older device and does not yet support the permissions needed for SDK 33+

Built from scratch over a 3-day sprint as a passion project, this is still very much in progress. In particular:

## TO DO
### Compatibility
- [ ] Update permission structure to support SDK 33+ as well as older versions
- [ ] Explore opportunities to use newer Compose elements on newer devices
- [ ] Deep test on newer devices
### Test coverage
> !! Top priority -- no tests currently
- [ ] App logic
- [ ] Repo logic
- [ ] DB logic
- [ ] App front-end
### Features
- [ ] Sorting options on game list view
- [ ] Filtering options on game list view
- [ ] "Add another" option after adding a new game
- [ ] Prevent randomizer from recommending same game again
- [ ] Clearer messaging/UI on GameBot view to require at least one selection in each category
- [ ] Support for attaching images from library, not just camera
- [ ] Allow user to create custom lists and select random from list
- [ ] Support for user-created tags and other values on games
### Code quality and documentation
- [ ] Expand top-level comments and add more inline as needed
- [ ] Better modularity of compose elements
- [ ] Handle universal style values more succinctly
- [ ] Add more detailed run/test/deploy instructions to this file
- [ ] Probably get this list into a set of tickets instead...

## License and Acknowledgements

This project is licensed under the terms of the MIT License

Copyright (c) 2024 Diana Patton-LoveCooksey

A huge thank-you to [Scott Stanchfield](https://www.javadude.com/), whose course on Android Development at Johns Hopkins got me started on this idea and informed how I designed the architecture. A modified portion of public code from his course materials is also used in the ViewModel as noted in the license and attribution in that file.
