BookTrack - Application Mobile de Suivi de Lecture
Description Générale :

BookTrack est une application mobile dédiée à la gestion des livres. Elle permet aux utilisateurs de gérer leur collection de livres physiques ou numériques, suivre leurs progrès de lecture et atteindre des objectifs de lecture annuels ou mensuels. L'application facilite l'organisation des livres et le suivi des lectures, tout en offrant des recommandations personnalisées.

Fonctionnalités Principales :
Ajout de Livres

Ajouter des livres en recherchant via l'API Google Books ou Open Library.
Possibilité d'ajouter manuellement un livre en entrant un ISBN.
Gestion de la Bibliothèque

Organiser les livres en différentes catégories :
Lus
En cours de lecture
À lire
Suivi d’Objectifs de Lecture

Fixer un objectif de lecture annuel ou mensuel.
Suivre l'évolution du nombre de livres lus par rapport à l'objectif défini.
Statistiques de Lecture

Visualiser des statistiques détaillées sur les progrès de lecture, comme le nombre de livres lus sur une période donnée.
Notifications

Recevoir des rappels réguliers pour encourager la lecture et aider à atteindre les objectifs.
Technologies Utilisées :
Kotlin : Langage principal utilisé pour le développement Android.
Room : Base de données locale pour stocker les informations des livres, des objectifs de lecture, et des statistiques.
Retrofit : Utilisé pour les appels API vers Google Books et Open Library.
MVVM (Model-View-ViewModel) : Architecture pour une gestion propre des données et de l'UI.
Fragments : Utilisation des fragments pour une gestion modulaire de l'interface utilisateur.
Notifications Locales : Pour envoyer des rappels à l'utilisateur concernant ses objectifs de lecture.


Structure du Projet
Room : Base de données pour gérer les livres, les objectifs de lecture et les catégories.
GoogleBooksAPI : Utilisée pour la recherche de livres via l'API de Google.
Fragments : Interface utilisateur divisée en fragments pour une meilleure gestion de la navigation.
MVVM : Utilisation du modèle Model-View-ViewModel pour séparer la logique métier, les données et l'interface utilisateur.

Améliorations Futures
Recommandations Personnalisées
Ajouter un système de recommandations de livres basé sur l’historique de lecture.
Synchronisation Cloud
Permettre la synchronisation des données sur plusieurs appareils via le cloud.

Suivi des lectures numériques
Ajouter des fonctionnalités de suivi pour les livres numériques (par exemple, synchronisation avec des plateformes de lecture).

Amélioration de l'UI/UX
Refonte de l'interface utilisateur pour une expérience plus fluide et moderne.
