(ns database.common.repository
  (:import org.bson.types.ObjectId)
  (:require
    [monger.core :as mg]
    [monger.collection :as mc]
    [monger.query :as mq]
    [utils.connection :as con]
    [utils.helpers :refer :all]))

(defn get-list-by-filter
  "Function for get collection documents by filter"
  [connection collection filter fields]
  (let [db (con/get-db-from-connection connection)]
    (if (nil? collection)
      (throw (Exception. "Not send collection name"))
      (mc/find-maps
        db
        collection
        (if (nil? filter) {} filter)
        (if (nil? fields) [] fields)))))

(defn get-collection-count
  "Function for get collection count"
  [connection collection filter]
  (let [db (con/get-db-from-connection connection)]
    (if (nil? collection)
      (throw (Exception. "Not send collection name"))
      (mc/count db collection (if (nil? filter) {} filter)))))

(defn get-collection-document
  "Function for get database single document from collection"
  [connection collection filter as-map fields]
  (let [db (con/get-db-from-connection connection)]
    (if (or (nil? collection) (nil? filter))
      (throw (ex-info
               "Not send collection name or filer"
               {:alias "can-not-get-collection-document"
                :info {:collection (not-send collection)
                       :name (not-send name)}}))
      (if (true? as-map)
        (mc/find-one-as-map db collection filter fields)
        (mc/find-one db collection filter fields)))))

(defn get-list-by-query
  "Function for get list collection documents by query"
  [connection opts filter sort fields]
  (let [db (con/get-db-from-connection connection)
        collection (get opts :collection nil )
        skip (get opts :skip 0)
        limit (get opts :limit 10)
        current-filter (if (nil? filter) {} filter)]
    (if (nil? collection)
      (throw (Exception. "Not send query options"))
      (mq/with-collection
          db
          collection
          (mq/find current-filter)
          (mq/skip skip)
          (mq/fields fields)
          (mq/limit limit)
          (mq/sort sort)))))

(defn create-document
  "Function for create document"
  [connection collection data]
  (let [db (con/get-db-from-connection connection)]
    (if (nil? collection)
      (throw (Exception. "Not send collection name"))
      (mc/insert-and-return db collection data))))

(defn update-document
  "Function for update collection document"
  [connection collection id data]
  (if (nil? id)
    (throw (Exception. "Not send document id"))
    (let [db (con/get-db-from-connection connection)
          id (if (string? id) (ObjectId. ^String id) id)]
      (if (nil? collection)
        (throw (Exception. "Not send collection name"))
        (mc/update-by-id db collection id data)))))

(defn remove-document-by-id
  "Function for remove document by id"
  [connection collection id]
  (if (nil? id)
    (throw (Exception. "Not send document id"))
    (let [db (con/get-db-from-connection connection)
          oid (if (string? id) (ObjectId. ^String id) id)]
      (mc/remove-by-id db collection oid))))