# Applications de référence #
  * ZBrush : http://www.pixologic.com/home.php
  * Sculptris : http://www.sculptris.com/
  * Holotoy : http://www.youtube.com/watch?v=NvCHHUN8nnE
  * Blender : http://www.blender.org/

# Fonctionnalités #
  * zbrush sculpture 3D augmentée
  * holotoy pour 3d holo, face tracking avec opencv, sculptris pour doigt et outils
  * interface de dessin, outils, doigt ou fin, application de motifs, pinceaux
  * zoom, pan, rotation (si pas compas)
  * suivi avec compas et accéléromètre pour impression de 3D (total ou juste décalage vis lat shake)
  * streaming données 3d suivant zone zoomée pour pas casser la ram
  * texture avec appareil photo
  * dessin sur matériau façon glaise avec rotation réelle physique autour d'un objet virtuel, pas de texture pour moins mémoire
  * pouvoir modéliser des visage ou des bâtiments suivant le zoom
  * sculpture de terrain pour aller dehors et tourner réellement autour des objets
  * rotation ou dessin via touch sur options
  * mode symétrique pour visage ou objets biologique
  * pouvoir exporter normale map ou fichier 3D dans format standard (3DS)
  * retour haptique lors sculpture via vibration (désactivable)
  * menu options standard
  * geotagger les sculpture avec historique
  * coupler avec modeleur de terrain pour batiments?
  * zoom multitouche
  * recentrage sur position courante sur appui long
  * filtrage des vibrations
  * gestion historique et undo/points de passage
  * tests unitaires intégrés/robot de compilation?
  * color picker pour matériau
  * integration à fond avec l'esprit android (pile backstack, parametre, dump paramètres, options,)
  * possibilité enregistrer vidéo et snapshots de la sculpture
  * rendu allégé pendant rotation autour sculpture puis affichage détaillé si stable ou sculpture en cours
  * possibilité de sculpter en tournant ou rotation bloquée au choix
  * mode démo type holotoy juste for fun
  * face tracking avec caméra avant
  * modèles de démo jolis intégré
  * test to speech, utilisation de jolies fontes
  * sculpture sensible à la force de la pression du doigt et à la largeur de la zone
  * mode peinture sur sculpture uniquement en mode matériau
  * benchmark pour performance 3D
  * mode rotation simplifié en polygones pour être fluide
  * minimiser temps de traitement des modifications quitte à recalcule lors du zoom
  * utilisation de lod précalculés
  * affichage de fps sur la 3D
  * superposition caméra pour sculpture plus facile
  * modifications vectorielle, stocker les coups de doigts sur la surface avec l'outil adapté, pas le mesh pour recalcul aisé
  * recherche de version sur internet et suggestion d'upgrade via market, maj obligatoire pour publier sur site
  * compatibilité ascendante du format interne et des mesh
  * possibilité d'avoir plusieurs formes mesh de départ (ou vectorielle même avec recalcul des actions)
  * travail sur formes fermées uniquement mais concaves (araignées )
  * lib 3D android pour le moteur 3D?
  * mode tour de potier ou le modèle tourne tout seul (sculpt and paint)
  * mode replay pour revoir la création de la scupture en accéléré (ou en temps réel avec datage des actions)
  * Mode Set As wallpaper avec capture écran (depuis OpenGL)
  * Mode tournebroche pour visualiser la sculpture (ou en fond écran animé)
  * Doc and view pour avoir plusieurs vues possible de la sculpture
  * update automatique ou sur demande avec status
  * Décomposition en packages autonomes et réutilisables pour d'autres projets
  * Livewallaper rotatif avec screenshots auto issue d'une rotation du modèle

# Liste des outils de sculpture #
  * zoom/ pan/ rotate
  * gonfler/dégonfler
  * monter/descendre zone
  * taille/forme pinceau
  * ciseler
  * subdiviser/simplifier tout/local
  * couleur matériau
  * brosses à peindre sur sculpture
  * export/import obj
  * nouvelle sphere/ nouveau plan
  * symétrie
  * wireframe
  * adoucir/grossir
  * ouvrir sauver local/site web publier
  * mode truesculpt (accéléro/gyra/compas) ou rotation manuelle

# Site web associé #
  * Site simple et léger pour être visible sur un mobile
  * Screenshots sur le site en page d'accueil
  * Site internet avec publication des sculptures (anonyme ou non) et classement,...
  * directement intégré au bazar
  * Importation depuis le site vers l'appli (site adapté pour écran android)
  * Stocker les modifications sur serveur pour travail collaboratif ) plusieurs sur même modèle
  * Upload de sculptures sur le site en php
  * Identification unique des personnes grâce à numéro de téléphone et hardware (pour être anonyme et unique). Possibilité de nommer sculpture et sculpteur en plus de l'ID anon
  * Possibilité de télécharger les sculptures pour les éditer ou de les visualiser en flash sur le site (format standard)

# Technologies utilisées #
  * opengl pur pour 3d et perfo (GLSurfaceView)
  * photo pour texture ou pour fond animé suivant axe (pour inspiration dans vraie vie)
  * sensor compas et accéléro pour rotation autour et vue perçu (voir API Demo)+ filtrage pour supprimer perturbation et mouvement fluide
  * visioconf pour facetrack
  * multitouch pour sculpture
  * gui toolkit pour interface et configuration cumulée avec 3D (superposé transparent, 3D en fond)
  * commutation auto écran à gérer correctement suivant mécanisme standard android
  * streaming vers disque
  * vibration pour retour haptique
  * geottagage avec GPS standard
  * text to speech
  * google app engine pour le site web
  * google analytics pour le suivi de fréquentation

# Commerce #
  * video de démonstration sur youtube
  * projet collaboratif en édition pour modification par tout le monde (même anonymes)
  * tuto flash
  * données de démo
  * Tutorial intégré à l'application lors du premier lancement (simple affichage de pages web ?)