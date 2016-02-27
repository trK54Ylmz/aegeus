/*
 * Copyright 2015 Tarık Yılmaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aegeus.core;

import com.aegeus.config.format.ConfigObject;
import com.aegeus.utils.ConfigUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AuthenticationConfiguration
{
    @Bean
    public ConfigObject config() {
        return ConfigUtils.getConfig();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public JdbcRealm realm() {
        String uri = String.format("jdbc:h2:tcp://localhost:%d/aegeus", config().getWorkflow().getDbPort());

        ConfigObject config = config();

        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(uri);
        ds.setUser(config.getWorkflow().getDbUser());
        ds.setPassword(config.getWorkflow().getDbPass());

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);

        JdbcRealm realm = new JdbcRealm();
        realm.setDataSource(ds);
        realm.setPermissionsLookupEnabled(true);
        realm.setAuthenticationQuery("SELECT pass FROM users WHERE user = ?");
        realm.setPermissionsQuery("SELECT p.permission FROM permissions p INNER JOIN users u ON p.user_id = u.id WHERE u.user = ?");
        realm.setUserRolesQuery("SELECT r.role FROM roles r INNER JOIN users u ON u.id = r.user_id WHERE u.user = ?");
        realm.setCredentialsMatcher(matcher);
        realm.init();

        return realm;
    }

    @Bean
    public WebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager(realm());
        manager.setCacheManager(new MemoryConstrainedCacheManager());

        /**
         * Set security manager
         */
        SecurityUtils.setSecurityManager(manager);

        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterBean() {
        Map<String, String> definition = new HashMap<>();
        definition.put("/", "authc, roles[admin]");
        definition.put("/login", "authc");

        /**
         * Create shiro servlet filter
         */
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setFilterChainDefinitionMap(definition);
        filter.setLoginUrl("/login");

        filter.setSecurityManager(securityManager());

        LogoutFilter logout = new LogoutFilter();
        logout.setRedirectUrl("/logout");

        Map<String, Filter> filters = new HashMap<>();
        filters.put("anon", new AnonymousFilter());
        filters.put("authc", new FormAuthenticationFilter());
        filters.put("logout", logout);
        filters.put("roles", new RolesAuthorizationFilter());
        filters.put("user", new UserFilter());

        filter.setFilters(filters);

        return filter;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }

    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
}