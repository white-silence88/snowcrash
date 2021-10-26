(ns database.single.languages-repository
  (:require
    [utils.constants :refer :all]
    [utils.helpers :as h]
    [utils.repository-helpers :as rh]))

(defn create-language
  "Function for create language in database"
  [connection data]
  (rh/create-document connection languages-collection-name data))

(defn find-language-by-id
  "Function for find language by id"
  [connection id fields]
  (rh/find-document
    connection
    languages-collection-name
    {:_id (h/value->object-id id) :status default-status }
    fields
    "Can not find language"
    "not-found"
    {:_id id :fields fields}))

(defn get-language-list
  "Function for get languages list"
  [connection limit skip fields]
  (rh/list-documents
    connection
    languages-collection-name
    limit
    skip
    {:status default-status}
    {}
    fields
    "Languages list not found"
    "not-found"
    {:limit limit :skip skip}))

(defn update-document
  "Function for update language document"
  [connection language-id to-update fields]
  (rh/update-document
    connection
    languages-collection-name
    language-id
    to-update
    fields
    "Can not find language document"
    "not-found"))

(defn get-total
  "Function for get count of language documents"
  [connection]
  (rh/get-total-documents connection languages-collection-name))