// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ExecuteSql.proto

// Protobuf Java Version: 3.25.5
package cn.ac.iscas.dmo.db.rpc.execute;

public interface ConnectionResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cn.ac.iscas.dmo.db.rpc.stream.ConnectionResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 status = 1;</code>
   * @return The status.
   */
  int getStatus();

  /**
   * <code>string message = 2;</code>
   * @return The message.
   */
  String getMessage();
  /**
   * <code>string message = 2;</code>
   * @return The bytes for message.
   */
  com.google.protobuf.ByteString
      getMessageBytes();

  /**
   * <code>string token = 3;</code>
   * @return The token.
   */
  String getToken();
  /**
   * <code>string token = 3;</code>
   * @return The bytes for token.
   */
  com.google.protobuf.ByteString
      getTokenBytes();
}
