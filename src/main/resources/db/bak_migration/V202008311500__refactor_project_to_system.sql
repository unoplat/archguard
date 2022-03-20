alter table `project_info` change `project_name` `system_name` varchar(256) not null;
rename table `project_info` to `system_info`;
alter table `dubbo_service_config` change `project_id` `system_id` bigint not null;
alter table `dubbo_reference_config` change `project_id` `system_id` bigint not null;
alter table `dubbo_module` change `project_id` `system_id` bigint not null;
alter table `dubbo_bean` change `project_id` `system_id` bigint not null;
alter table `Configure` change `project_id` `system_id` bigint not null;
alter table `JAnnotation` change `project_id` `system_id` bigint not null;
alter table `JClass` change `project_id` `system_id` bigint not null;
alter table `JField` change `project_id` `system_id` bigint not null;
alter table `JMethod` change `project_id` `system_id` bigint not null;
alter table `code_class_dependencies` change `project_id` `system_id` bigint not null;
alter table `code_class_fields` change `project_id` `system_id` bigint not null;
alter table `_ClassMethods` change `project_id` `system_id` bigint not null;
alter table `code_class_parent` change `project_id` `system_id` bigint not null;
alter table `code_method_callees` change `project_id` `system_id` bigint not null;
alter table `code_method_fields` change `project_id` `system_id` bigint not null;
alter table `class_coupling` change `project_id` `system_id` bigint not null;
alter table `class_metrics` change `project_id` `system_id` bigint not null;

alter table `JAnnotationValue` change `project_id` `system_id` bigint not null;
alter table `logic_module` change `project_id` `system_id` bigint not null;

