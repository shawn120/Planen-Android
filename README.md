# Planen

A multi-functional, Swiss-Army-knife style, plan manager app developed with Kotlin native android environment.

Planen covers three major kinds of plan types: to-do, deadline, and schedule all in one place.

> Planen\
> [ˈplaːnən]\
> To plan, to schedule in German

## Feature highlights

- Take three kinds of task type
- Deadline type can show progress according to the position of today between start day and the due day
  - Dynamic deadline progress reflecting user's day of choosing in month view: only on branch `deadline`
- Data are all saved in a local database
- Using Nager public holiday API to load into America Holiday when the app started
- Month view allows users to go to different days in the past or the future
- Today’s highlight allows users to see today’s tasks or a preferred range from today. -- preference page
- Swipe to delete tasks, double confirm, and then it will be removed from the database as well
- Allow user to share their today’s highlights, be about to launch another app, eg. message, to share
- Google sign-in, load into user’s profile picture, username, email address

## Future features/todos

- Clean codes and fix bugs
- Save user's tasks into their own database/google calendar